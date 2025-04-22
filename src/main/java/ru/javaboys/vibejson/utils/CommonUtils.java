package ru.javaboys.vibejson.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CommonUtils {

    public static String generateJsonSchemaByKjetland(Class<?> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        JsonNode jsonSchema = schemaGen.generateJsonSchema(clazz);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema);
    }

    public static String generateJsonSchemaByJackson(Class<?> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator schemaGen =
                new com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator(mapper);
        JsonSchema schema = schemaGen.generateSchema(clazz);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
    }

    public static String readFileFromClassPath(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    public static String minifyJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object jsonObject = mapper.readValue(json, Object.class);
            return mapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при минификации JSON", e);
        }
    }
}
