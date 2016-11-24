package demo;

import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.Map;

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
}