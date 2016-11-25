package demo.translator;

import demo.model.Review;
import demo.model.TranslatorPayload;
import demo.model.TranslatorResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Service
public class TranslatorService {
    private static final Logger logger = Logger.getLogger(TranslatorService.class);
    @Value("${translator.batchNumber}") Integer batchNumber;
    @Value("${translator.max.characters}") Integer maxCharacters;
    @Value("${translator.lang.from}") String langFrom;
    @Value("${translator.lang.to}") String langTo;

    @Autowired
    GoogleTranslateClient client;
    List<Batch> tmpBatchList;

    @PostConstruct
    void init(){
        tmpBatchList = new ArrayList<>(batchNumber);
    }

    public Iterator<Review> translate(Iterator<Review> reviews){
        ArrayBlockingQueue<Review> buffer = new ArrayBlockingQueue<>(batchNumber*2);
        TranslatorIterator iterator = new TranslatorIterator(buffer);
        new Thread(() -> {
            List<Review> currentReviews = new ArrayList<>(batchNumber);
            reviews.forEachRemaining(review->{
                if(addToBatchList(review, tmpBatchList)) {
                    currentReviews.add(review);
                }else{
                    List<Batch> toTranslate = tmpBatchList;
                    tmpBatchList = new ArrayList<>(batchNumber);
                    addToBatchList(review, tmpBatchList);
                    processBatchList(currentReviews, toTranslate, buffer);
                }
            });
            processBatchList(currentReviews, tmpBatchList, buffer);
            iterator.setDone(true);
        }).start();
        return iterator;
    }

    void processBatchList(List<Review> currentReviews, List<Batch> batchList, ArrayBlockingQueue<Review> buffer){
        List<String> translated = translate(batchList);
        logger.debug("BatchList Translated");
        //TODO: investigate
        if(translated.size()!=currentReviews.size())
            logger.debug("Lost Some Translations ?");
        //TODO:refactor this
        int size = translated.size() > currentReviews.size() ? currentReviews.size(): translated.size();

        range(0, size)
                .forEach(i -> {
                    Review current = currentReviews.get(i);
                    current.setText(translated.get(i));
                    insertToBuffer(current, buffer);
                });
        currentReviews.clear();
    }


    List<String> translate(List<Batch> batchList){
        List<ListenableFuture<ResponseEntity<TranslatorResponse>>> responseList = new ArrayList<>();
        logger.debug(format("Sending %s requests to Cloud Translation Service",batchList.size()));
        batchList.forEach(
                b->responseList.add(client.translate(payload(b)))
        );
        logger.debug("Waiting for response from Cloud Translation Service");
        return responseList.stream()
                .map(this::extractResultFromFuture)
                .flatMap(s -> Arrays.stream(s.split("\n")))
                .collect(toList());
    }

    String extractResultFromFuture(ListenableFuture<ResponseEntity<TranslatorResponse>> future) {
        Integer retryNumber = 3;
        Integer retry = 0;
        while(retry<=retryNumber){
            try {
                return future.get(10, SECONDS).getBody().getText();
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
               retry++;
                if(retry>retryNumber)
                    throw new RuntimeException("Google Translation Client Exception", e);
            }
        }
        return "";
    }

    TranslatorPayload payload(Batch batch) {
        return new TranslatorPayload(langFrom, langTo, batch.get());
    }

    boolean addToBatchList(Review review, List<Batch> batchList){
        Batch tail = getTail(batchList);
        if(tail.add(review))
            return true;
        if(batchList.size() < batchNumber) {
            batchList.add(createBatch(review));
            return true;
        }else
            return false;
    }

    Batch getTail(List<Batch> batchList){
        if(batchList.size()==0){
            Batch batch = new Batch(maxCharacters);
            batchList.add(batch);
        }
        return batchList.get(batchList.size() - 1);
    }

    Batch createBatch(Review review){
        Batch batch = new Batch(maxCharacters);
        batch.add(review);
        return batch;
    }

    void insertToBuffer(Review review, ArrayBlockingQueue<Review> buffer){
        //TODO: handle exceptions
        try {
            buffer.put(review);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
