package io.whatap.assignment.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.whatap.assignment.cmm.exception.CommonError;
import io.whatap.assignment.cmm.exception.ErrorResponse;
import io.whatap.assignment.cmm.exception.RestApiException;
import io.whatap.assignment.order.exception.OrderError;
import io.whatap.assignment.order.exception.ProductError;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * RestTemplate 에러 핸들러 정의
 */
@Slf4j
public class RestTemplateErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        log.debug("CodeCheck {}", httpResponse.getStatusCode());
        return (httpResponse.getStatusCode().is4xxClientError()
                || httpResponse.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().is4xxClientError()) {
            ErrorResponse errorResponse = getErrorResponse(httpResponse);
            ProductError error = ProductError.of(errorResponse.getCode());
            throw new RestApiException(error);
        }

        if (httpResponse.getStatusCode().is5xxServerError()) {
            //TODO .. 추가 멘트 필요
            throw new RestApiException(CommonError.INTERNAL_SERVER_ERROR);
        }


    }

    private ErrorResponse getErrorResponse(ClientHttpResponse httpResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStreamObject = httpResponse.getBody();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject,
                StandardCharsets.UTF_8))){
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            log.error("Error JSON str {}", sb);
            return mapper.readValue(sb.toString(),ErrorResponse.class);
        }
    }
}
