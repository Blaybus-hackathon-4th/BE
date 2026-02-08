package com.example.blaybus4th.domain.aiChat.dto.response;

import com.example.blaybus4th.domain.aiChat.entity.ChatMessage;
import com.example.blaybus4th.domain.aiChat.entity.ChatSession;
import com.example.blaybus4th.domain.aiChat.enums.SenderType;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatSessionResponse {

    private Long chatSessionId;

    private String chatSessionTitle;

    private List<ChatMessageDto> chatMessages;

    public static ChatSessionResponse from(ChatSession chatSession){
        ChatSessionResponse response = new ChatSessionResponse();
        response.chatSessionId = chatSession.getChatSessionId();
        response.chatSessionTitle = chatSession.getChatSessionTitle();
        response.chatMessages = chatSession.getChatMessages().stream()
                .map(ChatMessageDto::from)
                .toList();

        return response;
    }


    @Getter
    private static class ChatMessageDto{
        private String chatContent;
        private SenderType senderType;

        private static ChatMessageDto from(ChatMessage chatMessage){
            ChatMessageDto dto = new ChatMessageDto();
            dto.chatContent = chatMessage.getChatContent();
            dto.senderType = chatMessage.getSenderType();
            return dto;

        }

    }

}
