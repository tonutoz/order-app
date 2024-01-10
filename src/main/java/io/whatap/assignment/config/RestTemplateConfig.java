package io.whatap.assignment.config;

import io.whatap.assignment.rest.RestTemplateErrorHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateConfig 정의
 * 참고: https://mangkyu.tistory.com/256 [MangKyu's Diary:티스토리]
 */
@Configuration
@LoadBalancerClient(name="product-service")
public class RestTemplateConfig {
  //TODO .. log interceptor때문에 에러 핸들러 안먹히는듯...
  @Bean
  RestTemplate restTemplate() {
    RestTemplateBuilder builder = new RestTemplateBuilder();
    return new RestTemplateBuilder()
        .setConnectTimeout(Duration.ofSeconds(5))
        .setReadTimeout(Duration.ofSeconds(5))
        .errorHandler(new RestTemplateErrorHandler())
        //.additionalInterceptors(new LoggingInterceptor())

        .requestFactory(
            () -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
        .build();

  }

  @Slf4j
  static class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest req, byte[] body, ClientHttpRequestExecution ex)
        throws IOException, RestClientException {
      final String sessionNumber = makeSessionNumber();
      printRequest(sessionNumber, req, body);
      ClientHttpResponse response = ex.execute(req, body);
      printResponse(sessionNumber, response);
      return response;
    }

    private String makeSessionNumber() {
      return Integer.toString((int) (Math.random() * 1000000));
    }

    private void printRequest(final String sessionNumber, final HttpRequest req,
        final byte[] body) {
      log.info("[{}] URI: {}, Method: {}, Headers:{}, Body:{} ",
          sessionNumber, req.getURI(), req.getMethod(), req.getHeaders(),
          new String(body, StandardCharsets.UTF_8));
    }

    private void printResponse(final String sessionNumber, final ClientHttpResponse res)
        throws IOException {
      String body = new BufferedReader(
          new InputStreamReader(res.getBody(), StandardCharsets.UTF_8)).lines()
          .collect(Collectors.joining("\n"));

      log.info("[{}] Status: {}, Headers:{}, Body:{} ",
          sessionNumber, res.getStatusCode(), res.getHeaders(), body);
    }
  }

}
