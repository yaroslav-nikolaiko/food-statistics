package demo.parser;

import demo.model.Review;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.IntStream.range;

@Component
public class CSVParser implements Parser {
    private static final String ID = "Id";
    private static final String PROFILE_NAME = "ProfileName";
    private static final String PRODUCT_ID = "ProductId";
    private static final String TEXT = "Text";

    Map<String, Integer> fieldsMap = new HashMap<>();

    public CSVParser() {
        fieldsMap.put(ID, 0);
        fieldsMap.put(PROFILE_NAME, 3);
        fieldsMap.put(PRODUCT_ID, 1);
        fieldsMap.put(TEXT, 9);
    }

    @Override
    public Review parse(String line) {
        String[] fields = line.split(",");
        Review review  = new Review();

        String text = extractText(fields);

        //TODO: WTF??
        review.setProfileName(fields[fieldsMap.get(PROFILE_NAME)]);
        review.setItemID( fields[fieldsMap.get(PRODUCT_ID)]);
        if( ! fields[0].equals("Id")){
            review.setId(Long.valueOf(fields[fieldsMap.get(ID)]) );
        }
        review.setText(text);
        return review;
    }

    String extractText(String[] fields){
        Integer index = fieldsMap.get("Text");
        StringBuilder text = new StringBuilder(fields[index]);
        if(fields.length > index +1)
            range(index +1 , fields.length)
                    .forEach(i->
                            text.append(",")
                            .append(fields[i])
                    );
        return text.toString().replaceAll("^\"|\"$", "");
    }
}
