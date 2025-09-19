package ru.javaboys.vibejson.llm.dto;

import lombok.Data;

import java.util.List;

@Data
public class ValidationResult {
    private Boolean isValid;
    private List<String> errors;
}
