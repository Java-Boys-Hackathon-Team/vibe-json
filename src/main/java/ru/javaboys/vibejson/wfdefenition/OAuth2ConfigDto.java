package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@EntityDescription("Конфигурация OAuth2")
public class OAuth2ConfigDto {

    @NotBlank
    private String issuerLocation;

    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;

    @NotBlank
    private String grantType; // например, client_credentials
}
