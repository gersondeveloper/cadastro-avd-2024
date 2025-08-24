package com.gersondeveloper.cadastroavd2024.domain.interfaces;

import com.gersondeveloper.cadastroavd2024.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
