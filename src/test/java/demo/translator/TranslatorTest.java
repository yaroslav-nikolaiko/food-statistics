package demo.translator;

import demo.FoodReviewsApplication;
import demo.SparkService;
import demo.SparkServiceTest;
import demo.mock.GoogleTranslateMock;
import demo.model.Review;
import demo.model.TranslatorPayload;
import demo.model.TranslatorResponse;
import demo.parser.CSVParser;
import demo.translator.Translator;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.async.DeferredResult;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static demo.SparkServiceManager.getSparkService;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = FoodReviewsApplication.class)
public class TranslatorTest {
    GoogleTranslateMock translateMock;

    SparkService sparkService = getSparkService();

    @Autowired
    Translator translator;

    public TranslatorTest(){
        this.translateMock = new GoogleTranslateMock();
        this.translateMock.setResponseTime(0);
    }

    //TODO: This is only "smoke" test. Need to add much more
    @Test
    public void textShouldBeTranslatedTest(){
        Set<String> expected = expectedTranslation();
        List<Review> translated = translator.translate(sparkService.iterator());
/*        assertEquals(9, translated.size());
        translated.forEach(
                r->assertTrue(expected.contains(r.getText()))
        );*/
    }

    @Autowired
    TranslatorClient translatorClient;

    @Test
    @Ignore
    public void test() throws Exception {
        System.out.println("Before Request ");
        ListenableFuture<ResponseEntity<TranslatorResponse>> translate = translatorClient.translate(new TranslatorPayload("en", "ru", "dsfsdfsdf sdfsdf"));
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
        //System.out.println(translate);
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