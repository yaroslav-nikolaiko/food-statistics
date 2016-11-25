package demo.translator;

import demo.FoodReviewsApplication;
import demo.SparkService;
import demo.mock.GoogleTranslateMock;
import demo.model.Review;
import demo.model.TranslatorPayload;
import demo.model.TranslatorResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.*;

import static demo.SparkServiceManager.getSparkService;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = FoodReviewsApplication.class)
public class TranslatorTest {
    @Autowired
    GoogleTranslateMock translateMock;

    SparkService sparkService = getSparkService();

    @Autowired
    TranslatorService translator;


    //TODO: This is only "smoke" test. Need to add much more
    @Test
    public void textShouldBeTranslatedTest() throws IOException {
        Set<String> expected = expectedTranslation();
        Iterator<Review> reviewIterator = translator.translate(sparkService.iterator());


        //TODO: This is wrong but dont have time
        reviewIterator.forEachRemaining(r->{
            if(r!=null && r.getText()!=null)
                assertTrue(expected.contains(r.getText()));
        });
    }

    @Autowired
    GoogleTranslateClient googleTranslateClient;

    @Test
    @Ignore
    public void test() throws Exception {
        System.out.println("Before Request ");
        ListenableFuture<ResponseEntity<TranslatorResponse>> translate = googleTranslateClient.translate(new TranslatorPayload("en", "ru", "dsfsdfsdf sdfsdf"));
        /*while(true){
            System.out.println(translate.hasResult());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        System.out.println("After Request ");
        System.out.println(translate.get().getBody().getText());
        System.out.println("After REsponse ");
    }

    Set<String> expectedTranslation(){
        Set<String> result = new HashSet<>();
        textToTranslate().forEach(
                text->result.add(translateMock.translate(new TranslatorPayload("en", "fr", text)).getText())
        );
        return result;
    }



    List<String> textToTranslate(){
        List<String> toTranslate = new ArrayList<>();
        sparkService.iterator().forEachRemaining(r->toTranslate.add(r.getText()));
        return toTranslate;
    }
}