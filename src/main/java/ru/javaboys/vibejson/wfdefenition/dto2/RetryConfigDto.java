package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

import java.util.List;

@Data
@EntityDescription("Конфигурация повторов")
public class RetryConfigDto {

    /**
     * Максимальное количество попыток (включая первую)
     */
    @Min(1)
    private Integer maxAttempts = 3;

    /**
     * Задержка между попытками, ISO 8601 (например, PT15S, PT1M, P1D)
     */
    @Size(max = 50)
    private String delay = "PT30S";

    /**
     * Стратегия повтора: fixed (по умолчанию), exponential и т.п.
     */
    @NotBlank
    private String strategy = "fixed";

    /**
     * Список ошибок, по которым допускается повтор (коды или классы)
     */
    private List<@Size(max = 255) String> retryOnErrors;
}
