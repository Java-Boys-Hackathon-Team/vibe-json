package ru.javaboys.vibejson.llm.dto;

import lombok.Data;

import java.util.Map;

@Data
public class LLMResponseDto {

    // ex: "Да, привет у дела тебя?"
    private String LLMChatMsg;

    // string в формате json ex: заполненный результирующий workflow
    private String workflow;

    // универсальное поле для дополнительных параметров ответа от LLM
    private Map<String, Object> conversationCtx;

    // если что-то пошло не так, то здесь будет описание ошибки
    private Exception exception;
}
