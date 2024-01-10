package io.whatap.assignment.order.product;

import io.whatap.assignment.order.product.dto.ProductOrderRequest;
import io.whatap.assignment.order.product.dto.ProductOrderResponse;
import io.whatap.assignment.rest.RestTemplateService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductOrderService {

  private final RestTemplateService restTemplateService;
  @Value("${order-app.rest.gateway.url:'http://localhost:8888'}")
  private String apiGatewayBaseUrl;

  @Value("${order-app.rest.product.service.name:'product-service'}")
  private String productServiceBaseUrl;

  @Value("${order-app.rest.product.service.order.api:'/api/products/order'}")
  private String apiPath;

  private URI getUri() {
    return UriComponentsBuilder
        .fromUriString(apiGatewayBaseUrl+"/"+productServiceBaseUrl)
        .path(apiPath)
        .build().toUri();
  }

  public List<ProductOrderResponse> sendRequestProductOrder(final List<ProductOrderRequest> list) {

    return restTemplateService.postRequest(getUri(),list, new ParameterizedTypeReference<List<ProductOrderResponse>>(){});

  }

  public List<ProductOrderResponse> sendModifyProductOrder(final List<ProductOrderRequest> list) {
    return restTemplateService.putRequest(getUri(),list, new ParameterizedTypeReference<List<ProductOrderResponse>>(){});

  }

  public List<ProductOrderResponse> sendDeleteProductOrder(final List<ProductOrderRequest> list) {
    return restTemplateService.deleteRequest(getUri(),list, new ParameterizedTypeReference<List<ProductOrderResponse>>(){});

  }

}
