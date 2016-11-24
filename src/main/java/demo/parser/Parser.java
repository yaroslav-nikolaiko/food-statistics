package demo.parser;

import demo.model.Review;

import java.io.Serializable;

public interface Parser extends Serializable{
    Review parse(String line);
}
