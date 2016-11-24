package demo.translator;

import demo.model.Review;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TranslatorIterator implements Iterator<Review>{
    BlockingQueue<Review> buffer;
    AtomicBoolean isDone = new AtomicBoolean(false);

    public TranslatorIterator(BlockingQueue<Review> buffer){
        this.buffer = buffer;
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
            return buffer.poll(5, SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
