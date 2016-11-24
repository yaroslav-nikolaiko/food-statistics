package demo;

import org.junit.Test;

import java.net.URL;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SparkServiceTest {

    @Test
    public void mostActiveUsersTest(){
        SparkService sparkService = new SparkService();
        sparkService.setParser(new CSVParser());
        URL sample = this.getClass().getResource("/sample.csv");
        sparkService.load(sample);

        Map<String, Integer> users = sparkService.mostActiveUsers(2);

        assertEquals(2, users.size());

        assertTrue(users.containsKey("Karl"));
        assertEquals(Integer.valueOf(3), users.get("Karl"));

        assertTrue(users.containsKey("Natalia Corres"));
        assertEquals(Integer.valueOf(2), users.get("Natalia Corres"));
    }
}