package ru.javaboys.vibejson.wfdefenition;

import lombok.Data;

import java.util.Map;

@Data
@EntityDescription("Внедрение данных")
public class InjectDataDto {
    private Map<String, Object> data;
}
