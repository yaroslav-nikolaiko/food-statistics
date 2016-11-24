package demo.translator;

import demo.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Translator {
    @Value("${translator.batchNumber}") Integer batchNumber;
    @Value("${translator.max.characters}") Integer maxCharacters;
    //@Value("${translator.output.file}") String outputFile;

    @Autowired
    TranslatorClient client;

    public Iterator<Review> translate(Iterator<Review> reviews){
        AtomicInteger counter = new AtomicInteger();
        List<Batch> payloads = new ArrayList<>(batchNumber);
        ArrayBlockingQueue<Review> buffer = new ArrayBlockingQueue<>(100);
        TranslatorIterator iterator = new TranslatorIterator(buffer);
        Thread thread = new Thread(() -> {
            while (reviews.hasNext()) {
                Review review = reviews.next();
                try {
                    buffer.put(review);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //System.out.println(review.getText());
            }
            iterator.setDone(true);
        });
        thread.start();
/*        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return iterator;
    }

}
