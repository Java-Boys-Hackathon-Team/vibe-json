package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@EntityDescription("Авторизация по ключу доступа")
public class AccessKeyAuthDto {

    @NotBlank
    private String accessKey;

    @NotBlank
    private String secretKey;
}
