package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccessKeyAuthDto {

    @NotBlank
    private String accessKey;

    @NotBlank
    private String secretKey;
}
