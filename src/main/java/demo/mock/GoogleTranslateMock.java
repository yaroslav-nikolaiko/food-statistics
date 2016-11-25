package demo.mock;

import demo.model.TranslatorPayload;
import demo.model.TranslatorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path = "${google.translator.endpoint}")
public class GoogleTranslateMock {
    @Value("${google.translator.response.time.ms}")
    Integer responseTime;

    @RequestMapping(method = POST)
    public TranslatorResponse translate(@RequestBody TranslatorPayload payload){
        try {
            Thread.sleep(responseTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String input_lang = payload.getInput_lang();
        String output_lang = payload.getOutput_lang();

        String translated = Arrays.stream(payload.getText().split("\n"))
                .map(line -> format("Translated from %s to %s : %s",
                        input_lang,
                        output_lang,
                        line)
                ).collect(Collectors.joining("\n"));

        return new TranslatorResponse(translated);
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }
}
