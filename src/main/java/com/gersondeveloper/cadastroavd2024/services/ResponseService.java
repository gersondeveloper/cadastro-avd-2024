package com.gersondeveloper.cadastroavd2024.services;

import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserCreateResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private DataAccessException ex;
    private UserCreateResponse response;

    public ResponseService(DataAccessException ex, UserCreateResponse response) {}

}
