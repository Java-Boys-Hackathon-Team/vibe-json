package ru.javaboys.vibejson.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum SenderType implements EnumClass<String> {

    USER("USER"),
    BOT("BOT");

    private final String id;

    SenderType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SenderType fromId(String id) {
        for (SenderType at : SenderType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}