package ru.javaboys.vibejson.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CommonUtils {

    public static String toJson(Object o) {
        if (o == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }
}
