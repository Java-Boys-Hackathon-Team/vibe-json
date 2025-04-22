package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@EntityDescription("Ссылка на подключение S3")
public class S3ConnectionRefDto {

    private UUID id;

    @Size(max = 255)
    private String name;

    private Integer version;

    @Size(max = 255)
    private String tenantId;

}
