package com.example.blaybus4th.domain.aiChat.dto.request;

import lombok.Getter;


@Getter
public class AiChatRequest {

    private String userMessage;

    private Long chatSessionId;

    private ViewState viewState;

}
