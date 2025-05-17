package com.alok.home.email.configuration;

import com.alok.home.commons.security.HomeAuthTokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    @Value("${application.id}")
    private String clientId;

    @Value("${application.secret}")
    private String applicationSecret;

    @Bean
    RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    public RestTemplate homeApiRestTemplate(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${home-api.token.issuer}") String tokenIssuer,
            @Value("${home-api.token.url}") String tokenIssuerUrl,
            @Value("${home-api.token.scope}") String tokenScope,
            @Value("${home-api.token.audience}") String tokenAudience
    ) {
        return restTemplateBuilder.interceptors(homeAuthTokenInterceptor(
                        tokenIssuer, tokenIssuerUrl, tokenScope, tokenAudience,
                        clientId, applicationSecret
                ))
                .build();
    }

    private HomeAuthTokenInterceptor homeAuthTokenInterceptor(
            String tokenIssuer,
            String tokenIssuerUrl,
            String tokenScope,
            String tokenAudience,
            String clientId,
            String applicationSecret
    ) {
        return new HomeAuthTokenInterceptor(tokenIssuer, tokenIssuerUrl, tokenScope, tokenAudience, clientId, applicationSecret);
    }
}
