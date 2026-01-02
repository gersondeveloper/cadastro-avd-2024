package com.gersondeveloper.cadastroavd2024.infra.services;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateProductRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.ProductResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.Product;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.ProductRepository;
import com.gersondeveloper.cadastroavd2024.exceptions.EntityNotFoundException;
import com.gersondeveloper.cadastroavd2024.mappers.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    public Product createProduct(CreateProductRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Request cannot be null");
        var newProduct = mapper.toProduct(request);
        return repository.save(newProduct);
    }

    public Product getProductById(Long id) {
        if (id == null) throw new IllegalArgumentException("Product id cannot be null");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<ProductResponseDto> findAll(PageRequest pageRequest) {
        LOGGER.info("Listing all products");
        return mapper.toProductResponseDtoList(repository.findAll(pageRequest).getContent());
    }
}
