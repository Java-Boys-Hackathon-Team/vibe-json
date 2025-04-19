package ru.javaboys.vibejson.gpt_mts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.javaboys.vibejson.llm.mws.client.GPTMTSClient;
import ru.javaboys.vibejson.llm.mws.dto.ChatCompletionRequestDTO;
import ru.javaboys.vibejson.llm.mws.dto.ChatCompletionResponseDTO;
import ru.javaboys.vibejson.llm.mws.dto.GPTModelListDTO;
import ru.javaboys.vibejson.llm.mws.dto.MessageDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class GPTMTSServiceTest {

    @Autowired
    private GPTMTSClient modelClient;

    @Test
    @DisplayName("Получение списка моделей")
    public void getListModels() {
        GPTModelListDTO dto = modelClient.listModels();   // <‑‑ метод блокирующий
        Assertions.assertNotNull(dto);
    }

    @Test
    @DisplayName("Отправка нового сообщения")
    public void sendFirstMessage() {
        ChatCompletionRequestDTO req = new ChatCompletionRequestDTO();
        req.setModel("llama-3.3-70b-instruct");
        req.setTemperature(new BigDecimal(0));

        List<MessageDTO> list = new ArrayList<>();
        list.add(MessageDTO.builder().role("system").content("Ты помощник").build());
        list.add(MessageDTO.builder().role("use").content("У меня машина Ferrari").build());
        req.setMessages(list);

        ChatCompletionResponseDTO response = modelClient.getAnswer(req);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("Отправка сообщения с историей")
    public void sendMessagesWithHistory() {
        ChatCompletionRequestDTO req = new ChatCompletionRequestDTO();
        req.setModel("llama-3.3-70b-instruct");
        req.setTemperature(new BigDecimal(0));

        List<MessageDTO> list = new ArrayList<>();
        list.add(MessageDTO.builder().role("system").content("Ты помощник").build());
        list.add(MessageDTO.builder().role("use").content("У меня машина Ferrari").build());
        list.add(MessageDTO.builder().role("assistant").content("Вау, это потрясающе! Ferrari - это иконический и престижный бренд, известный своими высокопроизводительными и стильными автомобилями. Какой модель Ferrari у вас есть? Это классическая модель, такая как 250 GT или Dino, или современная модель, такая как 488 GTB или F8 Tributo?").build());
        list.add(MessageDTO.builder().role("use").content("Напомни, какая у меня машина?").build());
        req.setMessages(list);

        ChatCompletionResponseDTO response = modelClient.getAnswer(req);
        Assertions.assertNotNull(response);
    }

}
