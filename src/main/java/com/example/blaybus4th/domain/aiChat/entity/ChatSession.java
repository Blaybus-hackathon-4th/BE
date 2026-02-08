package com.example.blaybus4th.domain.aiChat.entity;

import com.example.blaybus4th.domain.member.entity.Member;
import com.example.blaybus4th.domain.object.entity.Object;
import com.example.blaybus4th.domain.object.entity.ObjectTag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "ChatSession")
@Getter
public class ChatSession {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatSessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id", nullable = false)
    private Object object;

    private String chatSessionTitle;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime chatSessionCreatedAt;


    @OneToMany(mappedBy = "chatSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("chatMessageId ASC")
    private List<ChatMessage> chatMessages = new ArrayList<>();



    public static ChatSession createChatSession(Member member, Object object) {
        ChatSession chatSession = new ChatSession();
        chatSession.member = member;
        chatSession.object = object;
        chatSession.chatSessionTitle = "";
        return chatSession;
    }

    public void addMessage(ChatMessage message) {
        this.chatMessages.add(message);
        message.setChatSession(this);
    }

    public void updateTitle(String title) {
        this.chatSessionTitle = title;
    }


}
