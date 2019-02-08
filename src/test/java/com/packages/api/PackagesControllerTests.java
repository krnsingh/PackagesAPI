package com.packages.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packages.api.dto.ProductResource;
import com.packages.api.dto.ProductsPackageResource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class PackagesControllerTests {


    List<ProductResource> allValidProducts;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(object);
    }

    @Before
    public void setup() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
        allValidProducts = getAllValidProducts();
    }

    @Test
    public void create_a_package_with_valid_products() throws Exception {
        //Given
        final var mapper = new ObjectMapper();
        final var request = new ProductsPackageResource();
        request.setProductResources(List.of(allValidProducts.get(0), allValidProducts.get(1)));
        request.setName("test package");
        request.setDescription("test description");

        //When
        final var resultActions = this.mockMvc.perform(post("/packages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(request)));

        //Then
        final var responseAsString = resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var pkgResponse = mapper.readValue(responseAsString, ProductsPackageResource.class);
        assertThat(pkgResponse.getIdentifier()).isNotEmpty();
        assertThat(pkgResponse.getName()).isEqualTo("test package");
        assertThat(pkgResponse.getDescription()).isEqualTo("test description");
        assertThat(pkgResponse.getIdentifier()).isNotEmpty();
        assertThat(pkgResponse.getProductResources()).isEqualTo(request.getProductResources());

    }

    @Test
    public void create_a_package_with_invalid_products() throws Exception {
        //Given
        final var product = allValidProducts.get(0);
        product.setIdentifier("invalid");
        final var request = new ProductsPackageResource();
        request.setProductResources(List.of(product));
        request.setName("test package");
        request.setDescription("test description");

        //When
        final var resultActions = this.mockMvc.perform(post("/packages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(request)));

        //Then
        resultActions.andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void retrieve_single_package() throws Exception {
        //Given
        final var mapper = new ObjectMapper();
        final var request = new ProductsPackageResource();
        request.setProductResources(List.of(allValidProducts.get(0), allValidProducts.get(1)));
        request.setName("test package");
        request.setDescription("test description");
        final var savedPkgJson = this.mockMvc.perform(post("/packages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        final var savedPkgResource = mapper.readValue(savedPkgJson, ProductsPackageResource.class);

        //When
        final var resultActions = this.mockMvc.perform(get("/packages/" + savedPkgResource.getIdentifier()));

        //Then
        final var responseAsString = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var pkgResponse = mapper.readValue(responseAsString, ProductsPackageResource.class);
        assertThat(pkgResponse.getIdentifier()).isNotEmpty();
        assertThat(pkgResponse.getIdentifier()).isEqualTo(savedPkgResource.getIdentifier());
        assertThat(pkgResponse.getName()).isEqualTo("test package");
        assertThat(pkgResponse.getDescription()).isEqualTo("test description");
        assertThat(pkgResponse.getProductResources()).isEqualTo(request.getProductResources());
    }

    @Test
    public void retrieve_invalid_package() throws Exception {
        //Given
        final var invalidId = "invalid";

        //When
        final var resultActions = this.mockMvc.perform(get("/packages/" + invalidId));

        //Then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
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

