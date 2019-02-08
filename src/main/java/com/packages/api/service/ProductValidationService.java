package com.packages.api.service;

import com.packages.api.dto.ProductResource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ProductValidationService {

    public boolean isValidList(List<ProductResource> products) {
        final var allValidProducts = getAllValidProducts();
        return allValidProducts.containsAll(products);
    }

    private List<ProductResource> getAllValidProducts() {
        final var restTemplate = new RestTemplate();
        final var responseEntity = restTemplate.exchange("https://product-service.herokuapp.com/api/v1/products",
                HttpMethod.GET, getHttpEntityWithBasicAuth(), new ParameterizedTypeReference<List<ProductResource>>() {
                });
        return responseEntity.getBody();
    }

    private HttpEntity<String> getHttpEntityWithBasicAuth() {
        final var creds = "user:pass";
        final var credsBytes = creds.getBytes();
        final var base64CredsBytes = Base64.encodeBase64(credsBytes);
        final var headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + new String(base64CredsBytes));

        return new HttpEntity(headers);
    }

}
