package com.example.blaybus4th.domain.aiChat.entity;


import com.example.blaybus4th.domain.aiChat.enums.SenderType;
import com.example.blaybus4th.domain.member.entity.Member;
import com.example.blaybus4th.domain.object.entity.Object;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Table(name = "ChatMessage")
public class ChatMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_session_id", nullable = false)
    private ChatSession chatSession;

    @Column(nullable = false)
    private String chatContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderType senderType;


    public static ChatMessage userMessage(String content) {
        ChatMessage message = new ChatMessage();
        message.chatContent = content;
        message.senderType = SenderType.USER;
        return message;
    }

    public static ChatMessage aiMessage(String content) {
        ChatMessage message = new ChatMessage();
        message.chatContent = content;
        message.senderType = SenderType.AI;
        return message;
    }


    protected void setChatSession(ChatSession session) {
        this.chatSession = session;
    }

}
