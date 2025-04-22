package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@EntityDescription("Определение авторизации S3")
public class S3AuthDefDto {

    @NotBlank
    private String type; // accessKey, IAM и др.

    @Valid
    private AccessKeyAuthDto accessKeyAuth;
}
