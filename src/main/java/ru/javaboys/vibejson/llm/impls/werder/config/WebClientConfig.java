package ru.javaboys.vibejson.llm.impls.werder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${mws.api.base-url}")
    private String baseUrl;

    @Value("${mws.api.key}")
    private String apiKey;

    @Bean(name = "mwsWebClient")
    public WebClient mwsWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }
}
