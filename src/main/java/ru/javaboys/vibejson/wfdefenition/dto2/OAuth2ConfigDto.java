package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
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
