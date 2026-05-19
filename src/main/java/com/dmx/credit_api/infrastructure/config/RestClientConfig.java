package com.dmx.credit_api.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Value("${external.frankfurter.base-url}")
    private String frankfurterBaseUrl;

    @Value("${external.frankfurter.connect-timeout-seconds:3}")
    private int connectTimeoutSeconds;

    @Value("${external.frankfurter.read-timeout-seconds:3}")
    private int readTimeoutSeconds;

    @Bean
    public RestClient frankfurterRestClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(connectTimeoutSeconds));
        factory.setReadTimeout(Duration.ofSeconds(readTimeoutSeconds));

        return RestClient.builder()
                .baseUrl(frankfurterBaseUrl)
                .requestFactory(factory)
                .build();
    }

}
