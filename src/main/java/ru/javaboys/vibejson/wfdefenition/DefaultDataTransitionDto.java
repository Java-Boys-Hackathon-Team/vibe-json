package ru.javaboys.vibejson.wfdefenition;


import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@EntityDescription("Переход к данным по умолчанию")
public class DefaultDataTransitionDto {

    @Size(max = 255)
    private String transition; // если null/пустое — WF завершается

    @Size(max = 400)
    private String conditionDescription;
}
