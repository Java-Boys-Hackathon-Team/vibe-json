package ru.javaboys.vibejson.view.vibejsonchat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Краткие сведения о схеме для заголовков и карточек в чате.
 * Схема разбирается без привязки к DTO: показать нужно и ту, что не проходит
 * валидацию или была поправлена вручную.
 */
record WorkflowSummary(String name, String description, int stepCount, List<String> starters, boolean valid) {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static WorkflowSummary of(String json) {
        if (json == null || json.isBlank()) {
            return new WorkflowSummary(null, null, 0, List.of(), false);
        }
        try {
            JsonNode root = MAPPER.readTree(json);
            List<String> starters = new ArrayList<>();
            root.path("details").path("starters")
                    .forEach(s -> starters.add(s.path("type").asText("")));
            JsonNode activities = root.path("compiled").path("activities");
            return new WorkflowSummary(
                    textOrNull(root.path("name")),
                    textOrNull(root.path("description")),
                    activities.isArray() ? activities.size() : 0,
                    starters.stream().filter(s -> !s.isBlank()).toList(),
                    true);
        } catch (Exception e) {
            return new WorkflowSummary(null, null, 0, List.of(), false);
        }
    }

    private static String textOrNull(JsonNode node) {
        String text = node.asText(null);
        return text == null || text.isBlank() ? null : text;
    }

    String displayName() {
        return name != null ? name : "Workflow";
    }
}
