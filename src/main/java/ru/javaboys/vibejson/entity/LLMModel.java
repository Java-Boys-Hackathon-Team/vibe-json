package ru.javaboys.vibejson.entity;

public enum LLMModel {
    MTC("lLMServiceMTS", "MTC"),
    DEMO("lLMServiceDemo", "DEMO"),
    CHATGPT("lLMServiceChatGPT", "CHAT_GPT");

    private final String beanName;
    private final String caption;

    LLMModel(String beanName, String caption) {
        this.beanName = beanName;
        this.caption = caption;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getCaption() {
        return caption;
    }
}
