package com.packages.api.service;

import com.packages.api.assembler.PackageResourceMapper;
import com.packages.api.dto.ProductsPackageResource;
import com.packages.api.exception.InvalidProductIdException;
import com.packages.api.exception.PackageNotFoundException;
import com.packages.api.repository.ProductsPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PackageJpaService {

    final private ProductsPackageRepository repository;
    final private PackageResourceMapper resourceMapper;
    final private ProductValidationService productValidationService;
    final private FixerIOService fixerIOService;

    @Autowired
    public PackageJpaService(final ProductsPackageRepository repository, final PackageResourceMapper resourceMapper,
                             final ProductValidationService productValidationService, final FixerIOService fixerIOService) {
        this.repository = repository;
        this.resourceMapper = resourceMapper;
        this.productValidationService = productValidationService;
        this.fixerIOService = fixerIOService;
    }

    public List<ProductsPackageResource> getAll() {
        final var packages = repository.findAll();
        return packages.stream()
                .map(p -> resourceMapper.toResource(p))
                .collect(Collectors.toList());
    }

    public ProductsPackageResource getById(final String apiId) {
        final var pkg = repository.findByApiId(apiId).orElseThrow(PackageNotFoundException::new);
        return resourceMapper.toResource(pkg);
    }

    public ProductsPackageResource getByIdAndCurrency(final String apiId, final String currency) {
        final var pkg = repository.findByApiId(apiId).orElseThrow(PackageNotFoundException::new);
        double currencyAmt = fixerIOService.convertToCurrency(currency, pkg.getTotalPrice());
        pkg.setTotalPrice(currencyAmt);
        return resourceMapper.toResource(pkg);
    }

    public ProductsPackageResource save(final ProductsPackageResource resource) {
        if (productValidationService.isValidList(resource.getProductResources())) {
            final var pkg = resourceMapper.toEntity(resource);
            final var savedPkg = repository.save(pkg);
            return resourceMapper.toResource(savedPkg);
        } else {
            throw new InvalidProductIdException();
        }
    }

    public ProductsPackageResource update(final ProductsPackageResource resource) {
        if (productValidationService.isValidList(resource.getProductResources())) {
            final var savedPkg = repository.findByApiId(resource.getIdentifier())
                    .orElseThrow(PackageNotFoundException::new);
            final var updatedPkg = resourceMapper.toEntity(resource);
            updatedPkg.setId(savedPkg.getId());
            return resourceMapper.toResource(repository.save(updatedPkg));
        } else {
            throw new InvalidProductIdException();
        }
    }


    public void delete(final String apiId) {
        repository.deleteByApiId(apiId);
    }

}
