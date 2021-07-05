package com.alemdianov.api.services;


import com.alemdianov.api.utils.RestTemplateResponseErrorHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;


@EnableScheduling
public class BaseService {
    final public static String BASE_URL = "http://95.111.235.18:8081/v2";

    protected RestTemplate restTemplate;

    public BaseService() {
        this.restTemplate = createTemplate();
    }

    private static RestTemplate createTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(15000);
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(15000);

        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        restTemplate.getMessageConverters().add(0, converter);

        return restTemplate;
    }

    public static HttpEntity getDefaultEntity() {
        return new HttpEntity(defaultHeaders());
    }

    public static HttpHeaders defaultHeaders(MediaType... extra) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api_key", "special-key");
        headers.set("Accept", "application/json");

        if (extra != null) {
            Arrays.stream(extra)
                    .forEach(mediaType -> headers.setContentType(mediaType));
        }

        return headers;
    }

    public static HttpHeaders formHeaders() {
        return defaultHeaders(APPLICATION_FORM_URLENCODED);
    }

    public static HttpHeaders mediaHeaders() {
        return defaultHeaders(MULTIPART_FORM_DATA);
    }

    public static HttpHeaders defaultRepoHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api_key", "special-key");
        headers.set("Accept", "application/vnd.github.mercy-preview+json");
        return headers;
    }
}