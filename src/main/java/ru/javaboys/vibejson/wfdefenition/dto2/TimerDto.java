package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TimerDto {

    /**
     * ISO 8601 duration формат, например: P0DT0H2M0S
     */
    @NotBlank
    private String timerDuration;
}
