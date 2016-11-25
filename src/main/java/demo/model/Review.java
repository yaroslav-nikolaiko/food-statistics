package demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data @AllArgsConstructor @NoArgsConstructor
public class Review implements Serializable{
    Long id;
    String profileName;
    String itemID;
    String text;

    public String toCsv() {
        return String.format("%s,%s,%s,%s\n", id,itemID, profileName, text);
    }
}
