package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@EntityDescription("Потребитель почты")
public class MailConsumerDto {

    @NotNull
    @Valid
    private MailConnectionDefDto connectionDef;

    @NotNull
    @Valid
    private MailFilterDto mailFilter;
}
