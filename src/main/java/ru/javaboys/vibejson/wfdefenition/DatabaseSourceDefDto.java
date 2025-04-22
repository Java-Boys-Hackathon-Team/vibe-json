package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@EntityDescription("Определение источника базы данных")
public class DatabaseSourceDefDto {

    @NotBlank
    @Size(max = 255)
    private String url;

    @NotBlank
    @Size(max = 255)
    private String className;

    @NotBlank
    @Size(max = 255)
    private String userName;

    @NotBlank
    @Size(max = 255)
    private String userPass;

}
