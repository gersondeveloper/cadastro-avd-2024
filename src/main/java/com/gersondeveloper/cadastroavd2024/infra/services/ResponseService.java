package com.gersondeveloper.cadastroavd2024.infra.services;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserCreateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseService {

  private DataAccessException ex;
  private UserCreateResponse response;

  public ResponseService(DataAccessException ex, UserCreateResponse response) {}
}
