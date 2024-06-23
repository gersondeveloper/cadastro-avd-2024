package com.gersondeveloper.cadastroavd2024.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UserCreateResponse {
    private int status;
    private String statusText;
    private boolean ok;
    private String url;
}
