package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Определение подключения S3")
public class S3ConnectionDefDto {

    @NotBlank
    private String endpoint;

    @Valid
    private S3AuthDefDto authDef;
}
