package com.example.blaybus4th.domain.aiChat.dto.request;


import lombok.Getter;

@Getter
public class AiDocentRequest {

    private Long objectId;

    private Long modelId;

    private Long selectedPartId; // 선택한 부품 id

    private String userMessage;

    private Long chatSessionId;

    private ViewState viewState;


}
