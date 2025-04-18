package ru.javaboys.vibejson.wfdefenition.dto2;


import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DefaultDataTransitionDto {

    @Size(max = 255)
    private String transition; // если null/пустое — WF завершается

    @Size(max = 400)
    private String conditionDescription;
}
