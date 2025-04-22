package ru.javaboys.vibejson.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

public class CommonUtils {

    public static String generateJsonSchemaByKjetland(Class<?> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        com.fasterxml.jackson.databind.JsonNode jsonSchema = schemaGen.generateJsonSchema(clazz);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema);
    }

    public static String generateJsonSchemaByJackson(Class<?> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        var schema = schemaGen.generateJsonSchema(clazz);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
    }
}
