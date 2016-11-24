package demo.translator;

import demo.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class Translator {
    private Integer batchNumber;
    private Integer maxCharacters;

    @Autowired
    TranslatorClient client;



    public List<Review> translate(Iterator<Review> reviews){
        return new ArrayList<>();
    }
}
