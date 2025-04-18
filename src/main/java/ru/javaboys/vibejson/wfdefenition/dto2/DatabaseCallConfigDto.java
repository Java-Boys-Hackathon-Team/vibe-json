package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import lombok.Data;


@Data
public class DatabaseCallConfigDto {

    @Valid
    private DatabaseCallDefDto databaseCallDef;

    @Valid
    private DataSourceDefDto dataSourceDef;

    private String sql;

    private String type;

    private Object parameters;
}
