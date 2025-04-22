package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@EntityDescription("Ссылка на подключение SAP")
public class SapConnectionRefDto {

    private UUID id;

    @Size(max = 255)
    private String name;

    private Integer version;

    @Size(max = 255)
    private String tenantId;

}
