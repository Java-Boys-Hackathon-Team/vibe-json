package ru.javaboys.vibejson.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.UUID;

@JmixEntity
@Table(name = "CHAT_MESSAGE", indexes = {
        @Index(name = "IDX_CHAT_MESSAGE_CONVERSATION", columnList = "CONVERSATION_ID"),
        @Index(name = "IDX_CHAT_MESSAGE_JSON_DSL_SCHEMA", columnList = "JSON_DSL_SCHEMA_ID")
})
@Entity
public class ChatMessage {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinColumn(name = "CONVERSATION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Conversation conversation;

    @JoinColumn(name = "JSON_DSL_SCHEMA_ID")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private JsonDslSchema jsonDslSchema;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private OffsetDateTime createdDate;

    @Column(name = "SENDER_TYPE")
    private String senderType;

    @Column(name = "CONTENT")
    @Lob
    private String content;

    public JsonDslSchema getJsonDslSchema() {
        return jsonDslSchema;
    }

    public void setJsonDslSchema(JsonDslSchema jsonDslSchema) {
        this.jsonDslSchema = jsonDslSchema;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SenderType getSenderType() {
        return senderType == null ? null : SenderType.fromId(senderType);
    }

    public void setSenderType(SenderType senderType) {
        this.senderType = senderType == null ? null : senderType.getId();
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}