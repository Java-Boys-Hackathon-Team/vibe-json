package ru.javaboys.vibejson.llm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.llm.client.GPTMTSClient;
import ru.javaboys.vibejson.llm.dto.ChatCompletionRequestDTO;
import ru.javaboys.vibejson.llm.dto.ChatCompletionResponseDTO;
import ru.javaboys.vibejson.llm.dto.GPTModelListDTO;

@Service
@RequiredArgsConstructor
public class GPTMTSService {

    private final GPTMTSClient gptClient;

    public GPTModelListDTO getGPTModels() {
        return gptClient.listModels();
    }

    public ChatCompletionResponseDTO sendNewMessage(ChatCompletionRequestDTO gptModelsDTO) {
        return gptClient.getAnswer(gptModelsDTO);
    }
}
