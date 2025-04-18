package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ParallelDto {

    @NotEmpty
    private List<@Size(min = 1, max = 255) String> branches;

    /**
     * Значения:
     * - "anyOf" (по умолчанию): переход происходит, как только одна из веток завершилась
     * - "allOf": ждем завершения всех веток
     */
    private String completionType;
}
