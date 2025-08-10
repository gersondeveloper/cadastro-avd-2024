package com.gersondeveloper.cadastroavd2024.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseCreateResponse {
    private int status;
    private String statusText;
    private boolean ok;
    private String url;
}
