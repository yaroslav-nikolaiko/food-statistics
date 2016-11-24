package demo;

import demo.model.TranslatorPayload;
import demo.model.TranslatorResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(url = "${google.translator.host}",path = "${google.translator.endpoint}", name = "translator-service")
public interface TranslatorClient {
    @RequestMapping(method = POST)
    TranslatorResponse translate(TranslatorPayload payload);
}
