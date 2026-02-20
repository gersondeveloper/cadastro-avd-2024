package com.gersondeveloper.cadastroavd2024.infra.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gersondeveloper.cadastroavd2024.domain.entities.Product;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.ProductRepository;
import com.gersondeveloper.cadastroavd2024.exceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

  @Autowired private ProductRepository repository;

  public Product createProduct(Product request) {
    if (request == null) throw new IllegalArgumentException("Request cannot be null");
    return repository.save(request);
  }

  public Product getProductById(Long id) {
    if (id == null) throw new IllegalArgumentException("Product id cannot be null");
    return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
  }

  public List<Product> findAll(PageRequest pageRequest) {
    log.info("Listing all products");
    return repository.findAll(pageRequest).getContent();
  }
}
