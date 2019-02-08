package com.packages.api.repository;

import com.packages.api.entities.ProductsPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ProductsPackageRepository extends JpaRepository<ProductsPackage, Long> {

    @Transactional
    Long deleteByApiId(final String apiId);

    Optional<ProductsPackage> findByApiId(final String apiId);

}
