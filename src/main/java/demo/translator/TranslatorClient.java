package demo.translator;

import demo.model.TranslatorPayload;
import demo.model.TranslatorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

@Service
public class TranslatorClient {
    @Value("${google.translator.host}") String host;
    @Value("${google.translator.endpoint}") String endpoint;

    AsyncRestTemplate restTemplate = new AsyncRestTemplate();

    public ListenableFuture<ResponseEntity<TranslatorResponse>> translate(TranslatorPayload payload){
        return restTemplate.postForEntity(url(), new HttpEntity<TranslatorPayload>(payload), TranslatorResponse.class);
    }

    String url(){
        return host + "/" + endpoint;
    }
}
