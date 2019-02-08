package com.packages.api.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products_package")
public class ProductsPackage {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String apiId;
    private String name;
    private String description;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Product> products = new ArrayList<>();

    private double totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductsPackage that = (ProductsPackage) o;
        return totalPrice == that.totalPrice &&
                id.equals(that.id) &&
                apiId.equals(that.apiId) &&
                name.equals(that.name) &&
                description.equals(that.description) &&
                products.equals(that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, apiId, name, description, products, totalPrice);
    }
}
