package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

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
