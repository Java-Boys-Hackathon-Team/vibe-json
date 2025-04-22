package ru.javaboys.vibejson.wfdefenition;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@EntityDescription("Определение источника данных")
public class DataSourceDefDto {

    @NotBlank
    private String url;

    @JsonAlias("userName")
    @NotBlank
    private String username;

    @JsonAlias("userPass")
    @NotBlank
    private String password;

    @NotBlank
    private String driverClassName;

    @Size(max = 255)
    private String type;

    @Size(max = 255)
    private String className;
}
