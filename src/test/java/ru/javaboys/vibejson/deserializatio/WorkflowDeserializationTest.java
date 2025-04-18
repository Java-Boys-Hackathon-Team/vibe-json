package ru.javaboys.vibejson.deserializatio;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.javaboys.vibejson.wfdefenition.dto2.WorkflowDefinitionDto;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class WorkflowDeserializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
            .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, true);

    /**
     * Универсальный метод для тестирования десериализации любого JSON-файла
     */
    private <T> void assertJsonMatchesDto(File jsonFile, Class<T> dtoClass) {
        try {
            objectMapper.readValue(jsonFile, dtoClass);
        } catch (IOException e) {
            fail("Не удалось десериализовать файл " + jsonFile.getName() + ": " + e.getMessage(), e);
        }
    }

    @Test
    @DisplayName("wf-1.json должен корректно десериализоваться в WorkflowDefinitionDto")
    public void testWf1Json() {
        File file = new File("src/test/resources/workflows/wf-1.json");
        assertJsonMatchesDto(file, WorkflowDefinitionDto.class);
    }

    @Test
    @DisplayName("wf-2.json должен корректно десериализоваться в WorkflowDefinitionDto")
    public void testWf2Json() {
        File file = new File("src/test/resources/workflows/wf-2.json");
        assertJsonMatchesDto(file, WorkflowDefinitionDto.class);
    }

    // И так далее...
}
