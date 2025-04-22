package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@EntityDescription("Определение подключения S3")
public class S3ConnectionDefDto {

    @NotBlank
    private String endpoint;

    @Valid
    private S3AuthDefDto authDef;
}
