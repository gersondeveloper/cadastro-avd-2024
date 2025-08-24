package com.gersondeveloper.cadastroavd2024.infra.services;

import com.gersondeveloper.cadastroavd2024.domain.dtos.ProductDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.Product;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.ProductRepository;
import com.gersondeveloper.cadastroavd2024.exceptions.EntityNotFoundException;
import com.gersondeveloper.cadastroavd2024.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public void createProduct(Product newProduct) {
        if (newProduct == null) throw new IllegalArgumentException("Product cannot be null");
        repository.save(newProduct);
    }

    public List<Product> getAllProducts() {
        return repository.findAll().stream()
                .toList();
    }

    public Product getProductById(Long id) {
        if (id == null) throw new IllegalArgumentException("Product id cannot be null");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }
}
