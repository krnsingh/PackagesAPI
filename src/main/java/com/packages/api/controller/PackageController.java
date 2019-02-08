package com.packages.api.controller;

import com.packages.api.dto.ProductsPackageResource;
import com.packages.api.service.PackageJpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/packages")
public class PackageController {


    final private PackageJpaService jpaService;

    @Autowired
    public PackageController(final PackageJpaService jpaService) {
        this.jpaService = jpaService;
    }

    @GetMapping
    public ResponseEntity<List<ProductsPackageResource>> retrieveAll() {
        return ResponseEntity.ok(jpaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductsPackageResource> retrieveById(@PathVariable String id,
                                                                @RequestParam(value = "currency", required = false)
                                                                        String currency) {
        if (currency == null) {
            return ResponseEntity.ok(jpaService.getById(id));
        } else {
            return ResponseEntity.ok(jpaService.getByIdAndCurrency(id, currency));
        }
    }

    @PutMapping
    public ResponseEntity<ProductsPackageResource> update(@RequestBody ProductsPackageResource resource) {
        final var savedPackage = jpaService.update(resource);
        return new ResponseEntity(savedPackage, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductsPackageResource> create(@RequestBody ProductsPackageResource resource) {
        final var savedPackage = jpaService.save(resource);
        return new ResponseEntity(savedPackage, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> create(@PathVariable String id) {
        jpaService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
