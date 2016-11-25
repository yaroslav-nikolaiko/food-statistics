package demo.translator;

import demo.model.TranslatorPayload;
import demo.model.TranslatorResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

@Service
public class GoogleTranslateClient {
    @Value("${google.translator.host}") String host;
    @Value("${google.translator.endpoint}") String endpoint;

    AsyncRestTemplate restTemplate;
    public GoogleTranslateClient(){
        restTemplate = new AsyncRestTemplate(asyncHttpRequestFactory());
    }

    private AsyncClientHttpRequestFactory asyncHttpRequestFactory() {
        return new HttpComponentsAsyncClientHttpRequestFactory(
                asyncHttpClient());
    }

    public CloseableHttpAsyncClient asyncHttpClient() {
            try {
                PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(
                        new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));
                connectionManager.setMaxTotal(200);
                connectionManager.setDefaultMaxPerRoute(200);
                RequestConfig config = RequestConfig.custom()
                        .setConnectTimeout(3000)
                        .build();

                return HttpAsyncClientBuilder
                        .create().setConnectionManager(connectionManager)
                        .setDefaultRequestConfig(config).build();
            } catch (IOReactorException e) {
                throw new RuntimeException(e);
            }
    }


    public ListenableFuture<ResponseEntity<TranslatorResponse>> translate(TranslatorPayload payload){
        return restTemplate.postForEntity(url(), new HttpEntity<>(payload), TranslatorResponse.class);
    }

    String url(){
        return host + "/" + endpoint;
    }
}
