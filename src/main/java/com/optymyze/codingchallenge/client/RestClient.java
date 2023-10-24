package com.optymyze.codingchallenge.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RestClient {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity callApi(String url, HttpMethod httpMethod, HttpEntity requestEntity, ParameterizedTypeReference responseType) {
        log.info("Calling rest api on url " + url);
        ResponseEntity responseEntity = restTemplate.exchange(url, httpMethod, requestEntity, responseType);
        log.info("Rest api " + url + " response status " + responseEntity.getStatusCode());
        return responseEntity;
    }

}
