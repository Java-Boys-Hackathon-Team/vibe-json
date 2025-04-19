package ru.javaboys.vibejson.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

@Converter(autoApply = false)
public class JsonbConverter implements AttributeConverter<String, Object> {

    @Override
    public Object convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            pgObject.setValue(attribute);
            return pgObject;
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка конвертации JSON -> PGobject", e);
        }
    }

    @Override
    public String convertToEntityAttribute(Object dbData) {
        return dbData != null ? dbData.toString() : null;
    }
}
