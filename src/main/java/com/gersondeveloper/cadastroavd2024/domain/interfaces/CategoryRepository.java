package com.gersondeveloper.cadastroavd2024.domain.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gersondeveloper.cadastroavd2024.domain.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}
