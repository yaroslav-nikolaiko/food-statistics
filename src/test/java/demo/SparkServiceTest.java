package demo;

import demo.model.Review;
import demo.parser.CSVParser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.avro.TypeEnum.c;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SparkServiceTest {
    static SparkService sparkService;

    @BeforeClass
    public static void init(){
        sparkService = new SparkService();
        sparkService.setParser(new CSVParser());
        URL sample = SparkServiceTest.class.getResource("/sample.csv");
        sparkService.load(sample);
    }

    @Test
    public void mostActiveUsersTest(){
        Map<String, Integer> users = sparkService.mostActiveUsers(2);

        assertEquals(2, users.size());

        assertTrue(users.containsKey("Karl"));
        assertEquals(Integer.valueOf(3), users.get("Karl"));

        assertTrue(users.containsKey("Natalia Corres"));
        assertEquals(Integer.valueOf(2), users.get("Natalia Corres"));
    }

    @Test
    public void mostCommentedFoodItemsTest(){
        Map<String, Integer> items = sparkService.mostCommentedFoodItems(2);

        assertEquals(2, items.size());

        assertTrue(items.containsKey("B006K2ZZ7K"));
        assertEquals(Integer.valueOf(4), items.get("B006K2ZZ7K"));

        assertTrue(items.containsKey("B000UA0QIQ"));
        assertEquals(Integer.valueOf(2), items.get("B000UA0QIQ"));
    }

    @Test
    public void mostUsedWordsInReviewTest(){
        Map<String, Integer> words = sparkService.mostUsedWordsInReview(3);

        assertEquals(3, words.size());
        //TODO: need more precise test
        words.forEach((word,count)->{
            assertTrue(count>0);
        });
    }

    @Test
    public void iteratorTest(){
        Iterator<Review> iterator = sparkService.iterator();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        iterator.forEachRemaining(r->atomicInteger.incrementAndGet());

        assertEquals(9, atomicInteger.get());
    }
}