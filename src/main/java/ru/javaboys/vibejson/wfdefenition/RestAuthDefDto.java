package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@EntityDescription("Определение авторизации REST")
public class RestAuthDefDto {

    @NotBlank
    private String type; // oauth2, basic и т.д.

    @Valid
    private OAuth2ConfigDto oauth2;

    // можно добавить: private BasicAuthDto basic; при необходимости
}
