package com.packages.api.assembler;

import com.packages.api.dto.ProductResource;
import com.packages.api.entities.Product;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ProductResourceMapper extends ResourceAssemblerSupport<Product, ProductResource> {

    public ProductResourceMapper() {
        super(Product.class, ProductResource.class);
    }

    @Override
    public ProductResource toResource(Product product) {
        final var resource = new ProductResource();
        resource.setIdentifier(product.getApiId());
        resource.setName(product.getName());
        resource.setUsdPrice(product.getUsdPrice());
        return resource;
    }

    public Product toEntity(ProductResource resource) {
        Product product = new Product();
        product.setApiId(resource.getIdentifier());
        product.setName(resource.getName());
        product.setUsdPrice(resource.getUsdPrice());
        return product;
    }
}
