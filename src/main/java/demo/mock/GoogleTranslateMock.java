package demo.mock;

import demo.model.TranslatorPayload;
import demo.model.TranslatorResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path = "${google.translator.endpoint}")
public class GoogleTranslateMock {
    @RequestMapping(method = POST)
    public TranslatorResponse translate(@RequestBody TranslatorPayload payload){
        return new TranslatorResponse(
                format("Translated from %s to %s : %s",
                        payload.getInput_lang(),
                        payload.getOutput_lang(),
                        payload.getText()));
    }
}
