package ru.javaboys.vibejson.wfdefenition;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@EntityDescription("Idoc")
public class IdocDto {

    @NotBlank
    private String xml;
}
