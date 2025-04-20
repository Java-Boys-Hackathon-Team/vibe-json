package ru.javaboys.vibejson.springai;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.javaboys.vibejson.llm.springai.ChatGPTService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ChatGPTTest {

    @Autowired
    private ChatGPTService chatGPTService;

    @Test
    @DisplayName("Промпт должен отправиться в Open AI ChatGPT")
    public void testSendPrompt() {
        String resp = chatGPTService.sendPrompt("Привет, как дела? Какую версию Java знаешь?");
        assertThat(resp).isNotEmpty();
    }
}
