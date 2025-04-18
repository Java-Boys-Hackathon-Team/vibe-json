package ru.javaboys.vibejson.llm.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.javaboys.vibejson.llm.dto.GPTModelsDTO;

@Component
@RequiredArgsConstructor
public class GPTMTSClient {

    private final WebClient mwsWebClient;

//    public GPTMTSClient(WebClient mwsWebClient) {
//        this.mwsWebClient = mwsWebClient;
//    }

    public GPTModelsDTO listModels() {
        return mwsWebClient
                .get()
                .uri("/v1/models")
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        resp -> resp.bodyToMono(String.class)
                                .map(body -> new IllegalStateException(
                                        "Ошибка MWS: " + resp.statusCode() + " – " + body)))
                .bodyToMono(GPTModelsDTO.class)
                .block();
    }

}
