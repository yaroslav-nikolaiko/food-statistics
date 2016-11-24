package demo;

import java.util.HashMap;
import java.util.Map;

public class CSVParser implements Parser {
    private static final String PROFILE_NAME = "ProfileName";
    private static final String PRODUCT_ID = "ProductId";
    private static final String TEXT = "Text";

    Map<String, Integer> fieldsMap = new HashMap<>();

    public CSVParser() {
        fieldsMap.put(PROFILE_NAME, 3);
        fieldsMap.put(PRODUCT_ID, 1);
        fieldsMap.put(TEXT, 9);
    }

    @Override
    public Review parse(String line) {
        String[] fields = line.split(",");
        Review review  = new Review();

        review.setProfileName(fields[fieldsMap.get("ProfileName")]);
        review.setItemID( fields[fieldsMap.get("ProductId")]);
        review.setText(fields[fieldsMap.get("Text")]);
        return review;
    }
}
