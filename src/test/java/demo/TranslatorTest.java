package demo;

import demo.mock.GoogleTranslateMock;
import demo.model.Review;
import demo.model.TranslatorPayload;
import demo.parser.CSVParser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = FoodReviewsApplication.class)
public class TranslatorTest {
    GoogleTranslateMock translateMock;

    static SparkService sparkService;

    @Autowired
    Translator translator;

    public TranslatorTest(){
        this.translateMock = new GoogleTranslateMock();
        this.translateMock.setResponseTime(0);
    }

    @BeforeClass
    public static void init(){
        sparkService = new SparkService();
        sparkService.setParser(new CSVParser());
        URL sample = SparkServiceTest.class.getResource("/sample.csv");
        sparkService.load(sample);
    }

    //TODO: This is only "smoke" test. Need to add much more
    @Test
    public void textShouldBeTranslatedTest(){
        Set<String> expected = expectedTranslation();
        List<Review> translated = translator.translate(sparkService.iterator());
        assertEquals(9, translated.size());
        translated.forEach(
                r->assertTrue(expected.contains(r.getText()))
        );
    }

/*    @Autowired
    TranslatorClient translatorClient;

    @Test
    public void test(){
        TranslatorResponse translate = translatorClient.translate(new TranslatorPayload("en", "ru", "dsfsdfsdf sdfsdf"));
        System.out.println(translate);
    }*/

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