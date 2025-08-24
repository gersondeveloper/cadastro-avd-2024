package com.gersondeveloper.cadastroavd2024.infra.services;

import com.gersondeveloper.cadastroavd2024.domain.dtos.response.CustomerResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private UserRepository repository;

    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        repository.save(user);
    }

    public List<CustomerResponseDto> findAllByRole(UserRole role) {
        return repository.findAllByRole(role);
    }
}
