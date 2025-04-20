package ru.javaboys.vibejson.wfdefenition.dto2;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Определение подключения SAP")
public class SapConnectionDefDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    private Map<String, String> props;
}
