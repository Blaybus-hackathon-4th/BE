package com.example.blaybus4th.AiChat.entity;


import com.example.blaybus4th.AiChat.enums.SenderType;
import com.example.blaybus4th.member.entity.Member;
import com.example.blaybus4th.object.entity.Object;
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
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id", nullable = false)
    private Object object;

    @Column(nullable = false)
    private String chatContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderType senderType;


}
