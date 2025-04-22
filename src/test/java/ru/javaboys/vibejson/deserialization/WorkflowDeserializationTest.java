package ru.javaboys.vibejson.deserialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.javaboys.vibejson.utils.CommonUtils;
import ru.javaboys.vibejson.wfdefenition.root.WorkflowDefinitionDto;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
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
        } catch (UnrecognizedPropertyException e) {
            String shortMessage = String.format(
                    "❌ В файле [%s] обнаружено неизвестное поле: \"%s\" в классе [%s]",
                    jsonFile.getName(),
                    e.getPropertyName(),
                    e.getReferringClass().getSimpleName()
            );
            fail(shortMessage);
        } catch (IOException e) {
            fail("❌ Ошибка при десериализации " + jsonFile.getName() + ": " + e.getMessage());
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

    @Test
    @DisplayName("wf-3.json должен корректно десериализоваться в WorkflowDefinitionDto")
    public void testWf3Json() {
        File file = new File("src/test/resources/workflows/wf-3.json");
        assertJsonMatchesDto(file, WorkflowDefinitionDto.class);
    }

    @Test
    @DisplayName("wf-4.json должен корректно десериализоваться в WorkflowDefinitionDto")
    public void testWf4Json() {
        File file = new File("src/test/resources/workflows/wf-4.json");
        assertJsonMatchesDto(file, WorkflowDefinitionDto.class);
    }

    @Test
    @DisplayName("wf-5.json должен корректно десериализоваться в WorkflowDefinitionDto")
    public void testWf5Json() {
        File file = new File("src/test/resources/workflows/wf-5.json");
        assertJsonMatchesDto(file, WorkflowDefinitionDto.class);
    }

    @Test
    @DisplayName("Создается валидная JSON Schema для WorkflowDefinitionDto")
    public void testCreateJSONSchema() throws Exception {
        String schema = CommonUtils.generateJsonSchema(WorkflowDefinitionDto.class);
        log.info("Generated schema:\n{}", schema);

        // Проверяем, что это валидный JSON
        try {
            JsonNode parsed = objectMapper.readTree(schema);
            assertNotNull(parsed, "JSON Schema не должна быть null");
            assertTrue(parsed.isObject(), "JSON Schema должна быть объектом (JSON Object)");
        } catch (Exception e) {
            fail("❌ Сгенерированная строка не является валидным JSON:\n" + e.getMessage());
        }
    }

}
