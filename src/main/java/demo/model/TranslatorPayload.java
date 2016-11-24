package demo.model;

import lombok.Data;

@Data
public class TranslatorPayload {
    String input_lang;
    String output_lang;
    String text;
}
