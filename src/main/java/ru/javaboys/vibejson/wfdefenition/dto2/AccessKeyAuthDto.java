package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Авторизация по ключу доступа")
public class AccessKeyAuthDto {

    @NotBlank
    private String accessKey;

    @NotBlank
    private String secretKey;
}
