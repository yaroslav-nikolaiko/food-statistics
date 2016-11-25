package demo.translator;

import demo.model.Review;
import org.junit.Test;

import static org.junit.Assert.*;

public class BatchTest {
    @Test
    public void testBatchCapacity(){
        Batch batch = new Batch(5);
        Review review = new Review(1L,null, null, "123");
        batch.add(new Review(1L,null, null, "1"));
        assertTrue(batch.canTake(review));
        batch.add(new Review(1L,null, null, "1"));
        assertFalse(batch.canTake(review));
    }

    @Test
    public void testBatchContent(){
        Batch batch = new Batch(10);
        Review review1 = new Review(1L,null, null, "123");
        Review review2 = new Review(1L, null, null, "qwe");
        batch.add(review1);
        batch.add(review2);

        assertEquals("123\nqwe\n", batch.text.toString());
    }
}