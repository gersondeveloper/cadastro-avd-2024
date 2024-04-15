package com.gersondeveloper.cadastroavd2024.interfaces;

import com.gersondeveloper.cadastroavd2024.domain.entities.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
