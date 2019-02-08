package com.packages.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties
public class ProductsPackageResource extends ResourceSupport {

    @JsonProperty("id")
    private String identifier;
    private String name;
    private String description;
    @JsonProperty("products")
    private List<ProductResource> productResources;
    private double totalPrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProductResource> getProductResources() {
        return productResources;
    }

    public void setProductResources(List<ProductResource> productResources) {
        this.productResources = productResources;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductsPackageResource that = (ProductsPackageResource) o;
        return Double.compare(that.totalPrice, totalPrice) == 0 &&
                identifier.equals(that.identifier) &&
                name.equals(that.name) &&
                description.equals(that.description) &&
                productResources.equals(that.productResources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identifier, name, description, productResources, totalPrice);
    }
}
