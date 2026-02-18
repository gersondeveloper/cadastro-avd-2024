package com.gersondeveloper.cadastroavd2024.domain.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gersondeveloper.cadastroavd2024.domain.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}
