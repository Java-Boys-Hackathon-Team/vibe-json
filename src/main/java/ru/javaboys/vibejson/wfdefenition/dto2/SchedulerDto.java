package ru.javaboys.vibejson.wfdefenition.dto2;

import java.time.ZonedDateTime;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Планировщик")
public class SchedulerDto {

    @NotBlank
    @Size(max = 255)
    private String type; // "cron"

    @NotNull
    @Valid
    private CronDto cron;

    @NotNull
    private ZonedDateTime startDateTime;

    private ZonedDateTime endDateTime;

    private Map<String, Object> outputTemplate;
}

