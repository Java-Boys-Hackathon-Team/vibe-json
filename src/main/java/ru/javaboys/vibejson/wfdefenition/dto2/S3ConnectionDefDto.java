package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class S3ConnectionDefDto {

    @NotBlank
    private String endpoint;

    @Valid
    private S3AuthDefDto authDef;
}
