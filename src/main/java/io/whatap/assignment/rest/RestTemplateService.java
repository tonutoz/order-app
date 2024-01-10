package io.whatap.assignment.rest;

import java.net.URI;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestTemplateService {

    private final RestTemplate restTemplate;

    public <T,R> R getRequest(final URI uri, final T reqBody, ParameterizedTypeReference<R> responseType) {
        return doRest(uri,reqBody,responseType,HttpMethod.GET);
    }

    public <T,R> R postRequest (final URI uri, final T reqBody, ParameterizedTypeReference<R> responseType) {
        return doRest(uri,reqBody,responseType,HttpMethod.POST);
    }

    public <T,R> R putRequest (final URI uri, final T reqBody, ParameterizedTypeReference<R> responseType) {
        return doRest(uri,reqBody,responseType,HttpMethod.PUT);
    }

    public <T,R> R deleteRequest (final URI uri, final T reqBody, ParameterizedTypeReference<R> responseType) {
        return doRest(uri,reqBody,responseType,HttpMethod.DELETE);
    }

    private <T,R> R doRest (final URI uri,final T reqBody, ParameterizedTypeReference<R> responseType, final HttpMethod method) {
        HttpHeaders headers = prepareHttpHeader();
        HttpEntity<T> entity = new HttpEntity<>(reqBody, headers);

        ResponseEntity<R> result  = restTemplate.exchange(uri, method,entity,responseType );

        return result.getBody();
    }

    private HttpHeaders prepareHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
