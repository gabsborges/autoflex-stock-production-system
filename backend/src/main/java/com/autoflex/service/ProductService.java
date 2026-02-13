package com.autoflex.service;

import com.autoflex.entity.Product;
import com.autoflex.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;

    @Transactional
    public Product create(Product product) {
        repository.persist(product);
        return product;
    }

    public List<Product> listAll() {
        return repository.listAll();
    }
}
