package demo.translator;

import demo.model.Review;
import demo.model.TranslatorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Batch {
    public static int maxCharacters;
    StringBuilder payload = new StringBuilder();
    List<Review> reviews = new ArrayList<>();
    ListenableFuture<ResponseEntity<TranslatorResponse>> translation;

    public Batch() {
    }

    public Batch(Review review) {
        add(review);
    }

    public boolean canTake(Review review){
        return review.getText().length() + payload.length() <= maxCharacters;
    }

    public boolean add(Review review){
        if(isBlank(review.getText()) || review.getText().length() >= maxCharacters)
            return true;
        boolean notFull = canTake(review);
        if(notFull) {
            reviews.add(review);
            if(payload.length()>0)
                payload.append("\n");
            payload.append(review.getText());
        }
        return notFull;
    }

    public List<Review> getTranslatedReviews(){
        String[] translated = extractResultFromFuture().split("\n");
        for (int i=0; i<translated.length; i++)
            reviews.get(i).setText(translated[i]);
        return reviews;
    }


    public String getText(){
        return payload.toString();
    }

    void setTranslation(ListenableFuture<ResponseEntity<TranslatorResponse>> translation) {
        this.translation = translation;
    }

    String extractResultFromFuture() {
        Integer retryNumber = 3;
        Integer retry = 0;
        while(retry<=retryNumber){
            try {
                return translation.get(10, SECONDS).getBody().getText();
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                retry++;
                if(retry>retryNumber)
                    throw new RuntimeException("Google Translation Client Exception", e);
            }
        }
        return "";
    }
}
