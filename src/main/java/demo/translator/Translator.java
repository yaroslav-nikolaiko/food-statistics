package demo.translator;

import demo.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Translator {
    @Value("${translator.batchNumber}") Integer batchNumber;
    @Value("${translator.max.characters}") Integer maxCharacters;
    @Value("${translator.output.file}") String outputFile;

    @Autowired
    TranslatorClient client;

    public void translate(Iterator<Review> reviews){
        AtomicInteger counter = new AtomicInteger();
        List<Batch> payloads = new ArrayList<>(batchNumber);
        while(reviews.hasNext()){
            Review review = reviews.next();
        }
    }

}
