package ru.javaboys.vibejson.llm.service;

import io.micrometer.core.instrument.util.IOUtils;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Service("lLMServiceDemo")
public class LLMServiceDemo implements LLMService {

     private final Faker faker = new Faker();
     private final AtomicInteger counter = new AtomicInteger();

     private String workflowTemplate;

     @PostConstruct
     protected void init() {
          try (InputStream in = getRandomWorkflowResource().getInputStream()) {
               workflowTemplate = IOUtils.toString(in, StandardCharsets.UTF_8);
          } catch (Exception e) {
               throw new IllegalStateException("Не удалось загрузить workflow‑шаблон", e);
          }
     }

     @Override
     public LLMResponseDto userPromptToWorkflow(Conversation conversation, String prompt) {
          int call = counter.incrementAndGet();

          String chatMsg = faker.harryPotter().quote();

          String workflow = (call % 6 == 0)
                  ? workflowTemplate
                  : null;

          LLMResponseDto dto = new LLMResponseDto();
          dto.setLLMChatMsg(chatMsg);
          dto.setWorkflow(workflow);
          dto.setConversationCtx(null);  // не используем
          dto.setException(null);

          return dto;
     }


     private Resource getRandomWorkflowResource() {
          int randomIndex = ThreadLocalRandom.current().nextInt(1, 6);
          String path = String.format("wf-defenition/examples/wf-%d.json", randomIndex);
          return new FileSystemResource(path);
     }
}
