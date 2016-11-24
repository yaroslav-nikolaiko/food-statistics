package demo.translator;

import demo.model.Review;

public class Batch {
    int maxCharacters;
    StringBuilder text = new StringBuilder();

    public Batch(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public boolean canTake(Review review){
        return review.getText().length() + text.length() <= maxCharacters;
    }

    public boolean add(Review review){
        boolean notFull = canTake(review);
        if(notFull)
            text.append(review.getText()).append("\n");
        return notFull;
    }
}
