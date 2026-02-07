package com.example.blaybus4th.domain.aiChat.dto.response;

import com.example.blaybus4th.domain.aiChat.dto.AiCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.List;

@Getter
public class AiChatResponse {

    private String aiMessage;

    private List<AiCommand> commands;



}
