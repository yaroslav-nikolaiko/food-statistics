package demo.translator;

import demo.model.Review;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TranslatorIterator implements Iterator<Review>{
    BlockingQueue<Review> buffer;
    AtomicBoolean isDone = new AtomicBoolean(false);

    public TranslatorIterator(int batchNumber){
        this.buffer = new ArrayBlockingQueue<>(batchNumber*2);
    }

    public void setDone(boolean isDone){
        this.isDone.set(isDone);
    }

    @Override
    public boolean hasNext() {
        return ! buffer.isEmpty() || ! isDone.get();
    }

    @Override
    public Review next() {
        //TODO: refactor exception handling
        try {
            return buffer.poll(10, SECONDS);
            //return buffer.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(List<Review> reviews){
        reviews.forEach(r -> {
            try {
                buffer.put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
