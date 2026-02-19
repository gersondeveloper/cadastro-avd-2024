package com.gersondeveloper.cadastroavd2024.infra.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateProductRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.ProductResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.Product;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.ProductRepository;
import com.gersondeveloper.cadastroavd2024.exceptions.EntityNotFoundException;
import com.gersondeveloper.cadastroavd2024.mappers.ProductMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

  @Autowired private ProductRepository repository;

  @Autowired private ProductMapper mapper;

  public Product createProduct(CreateProductRequest request) {
    if (request == null) throw new IllegalArgumentException("Request cannot be null");
    var newProduct = mapper.toProduct(request);
    return repository.save(newProduct);
  }

  public Product getProductById(Long id) {
    if (id == null) throw new IllegalArgumentException("Product id cannot be null");
    return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
  }

  public List<ProductResponse> findAll(PageRequest pageRequest) {
    log.info("Listing all products");
    return mapper.toProductResponseList(repository.findAll(pageRequest).getContent());
  }
}
