package ru.javaboys.vibejson.deserialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.javaboys.vibejson.wfdefenition.root.WorkflowDefinitionDto;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class WorkflowDeserializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
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
    @DisplayName("from-model.json должен корректно десериализоваться в WorkflowDefinitionDto")
    public void testFromModelJson() {
        File file = new File("src/test/resources/workflows/from-model.json");
        assertJsonMatchesDto(file, WorkflowDefinitionDto.class);
    }
}
