package com.gersondeveloper.cadastroavd2024.domain.dtos.response;

public record CreateResponse<T>(int status, String statusText, boolean ok, String url, T obj) {}
