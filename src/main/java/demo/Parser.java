package demo;

import java.io.Serializable;

public interface Parser extends Serializable{
    Review parse(String line);
}
