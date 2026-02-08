package com.example.blaybus4th.domain.aiChat.dto.response;

import com.example.blaybus4th.domain.aiChat.dto.AiCommand;
import com.example.blaybus4th.domain.aiChat.entity.ChatSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiChatResponse {

    private Long chatSessionId;

    private String chatSessionTitle;

    private String aiMessage;

    private List<AiCommand> commands;


    public static AiChatResponse from(AiChatResponse response, ChatSession chatSession){
        return new AiChatResponse(
                chatSession.getChatSessionId(),
                chatSession.getChatSessionTitle(),
                response.getAiMessage(),
                response.getCommands()
        );
    }


}

