package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Файл S3")
public class S3FileDto {

    @NotBlank
    @Size(max = 255)
    private String filePath;

    @NotBlank
    @Size(max = 255)
    private String content;
}
