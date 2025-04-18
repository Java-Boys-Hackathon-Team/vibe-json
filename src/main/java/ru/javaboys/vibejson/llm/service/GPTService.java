package ru.javaboys.vibejson.llm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.llm.client.GPTMTSClient;
import ru.javaboys.vibejson.llm.dto.GPTModelsDTO;

@Service
@RequiredArgsConstructor
public class GPTService {

    private final GPTMTSClient gptClient;

    public GPTModelsDTO getGPTModels() {
        return gptClient.listModels();
    }
}
