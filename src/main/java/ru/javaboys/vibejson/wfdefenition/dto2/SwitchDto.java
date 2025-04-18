package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SwitchDto {

    @NotEmpty
    @Valid
    private List<DataConditionDto> dataConditions;

    @NotNull
    @Valid
    private DefaultConditionDto defaultCondition;
}
