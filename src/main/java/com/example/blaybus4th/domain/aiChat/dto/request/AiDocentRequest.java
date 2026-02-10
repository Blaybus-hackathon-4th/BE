package com.example.blaybus4th.domain.aiChat.dto.request;


import lombok.Getter;

@Getter
public class AiDocentRequest {

    private Long objectId;

    private Long modelId;

    private String userMessage;

    private Long chatSessionId;

    private ViewState viewState;


}
