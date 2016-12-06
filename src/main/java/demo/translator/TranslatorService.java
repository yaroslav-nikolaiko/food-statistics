package demo.translator;

import demo.model.Review;
import demo.model.TranslatorPayload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Service
public class TranslatorService {
    private static final Logger logger = Logger.getLogger(TranslatorService.class);
    @Value("${translator.batchNumber}") Integer batchNumber;
    @Value("${translator.lang.from}") String langFrom;
    @Value("${translator.lang.to}") String langTo;

    @Autowired
    GoogleTranslateClient client;
    TranslatorIterator resultIterator;

    @PostConstruct
    void init(){
        resultIterator = new TranslatorIterator(batchNumber);
    }

    @Value("${translator.max.characters}")
    void setMaxCharacters(Integer maxCharacters){
        Batch.maxCharacters = maxCharacters;
    }

    public Iterator<Review> translate(Iterator<Review> reviews){
        List<Batch> tmpBatchList = new ArrayList<>(batchNumber);
        new Thread(() -> {
            reviews.forEachRemaining(review->{
                if( ! addToBatchList(review, tmpBatchList)) {
                    resultIterator.add(translate(tmpBatchList));
                    tmpBatchList.clear();
                    addToBatchList(review, tmpBatchList);
                }
            });
            resultIterator.add(translate(tmpBatchList));
            resultIterator.setDone(true);
        }).start();
        return resultIterator;
    }

    List<Review> translate(List<Batch> batchList){
        logger.debug(format("Sending %s requests to Cloud Translation Service", batchList.size()));
        batchList.forEach(
                b->b.setTranslation(client.translate(new TranslatorPayload(langFrom, langTo, b.getText())))
        );
        logger.debug("Waiting for response from Cloud Translation Service");
        return batchList.stream()
                .flatMap(item -> item.getTranslatedReviews().stream())
                .collect(toList());
    }

    boolean addToBatchList(Review review, List<Batch> batchList){
        Batch tail = getTail(batchList);
        if(tail.add(review))
            return true;
        if(batchList.size() < batchNumber) {
            batchList.add(new Batch(review));
            return true;
        }else
            return false;
    }

    Batch getTail(List<Batch> batchList){
        if(batchList.size()==0){
            Batch batch = new Batch();
            batchList.add(batch);
        }
        return batchList.get(batchList.size() - 1);
    }
}
