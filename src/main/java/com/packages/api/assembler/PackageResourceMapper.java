package com.packages.api.assembler;

import com.packages.api.dto.ProductResource;
import com.packages.api.dto.ProductsPackageResource;
import com.packages.api.entities.Product;
import com.packages.api.entities.ProductsPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PackageResourceMapper extends ResourceAssemblerSupport<ProductsPackage, ProductsPackageResource> {

    private final ProductResourceMapper productResourceMapper;

    @Autowired
    public PackageResourceMapper(final ProductResourceMapper productResourceMapper) {
        super(ProductsPackage.class, ProductsPackageResource.class);
        this.productResourceMapper = productResourceMapper;
    }

    @Override
    public ProductsPackageResource toResource(ProductsPackage pkg) {
        final var resource = new ProductsPackageResource();
        resource.setIdentifier(pkg.getApiId());
        resource.setName(pkg.getName());
        resource.setDescription(pkg.getDescription());
        resource.setTotalPrice(pkg.getTotalPrice());
        final var products = pkg.getProducts().stream()
                .map(p -> productResourceMapper.toResource(p))
                .collect(Collectors.toList());
        resource.setProductResources(products);
        return resource;
    }

    public ProductsPackage toEntity(ProductsPackageResource resource) {
        final var pkg = new ProductsPackage();
        pkg.setName(resource.getName());

        if (StringUtils.isEmpty(resource.getIdentifier())) {
            final var apiId = UUID.randomUUID().toString().replace("-", "");
            pkg.setApiId(apiId);
        } else {
            pkg.setApiId(resource.getIdentifier());
        }

        pkg.setDescription(resource.getDescription());
        pkg.setTotalPrice(calculateTotalPrice(resource));
        pkg.setProducts(getProducts(resource.getProductResources()));
        return pkg;
    }

    private double calculateTotalPrice(ProductsPackageResource resource) {
        return resource.getProductResources().stream()
                .map(r -> r.getUsdPrice())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private List<Product> getProducts(List<ProductResource> resources) {
        return resources.stream()
                .map(p -> productResourceMapper.toEntity(p))
                .collect(Collectors.toList());
    }

}
