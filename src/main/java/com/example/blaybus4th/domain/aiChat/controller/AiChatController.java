package com.example.blaybus4th.domain.aiChat.controller;

import com.example.blaybus4th.domain.aiChat.dto.request.AiChatRequest;
import com.example.blaybus4th.domain.aiChat.dto.response.ChatSessionResponse;
import com.example.blaybus4th.domain.aiChat.service.AiChatService;
import com.example.blaybus4th.global.annotation.InjectMemberId;
import com.example.blaybus4th.global.apiPayload.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;

    /**
     * Ai 대화 요청
     * 담당자 : 김영욱
     */
    @PostMapping("/{objectId}")
    public ApiResponse<?> aiChat(
            @PathVariable Long objectId,
            @InjectMemberId Long memberId,
            @RequestBody AiChatRequest request
    ) throws JsonProcessingException {
        return ApiResponse.onSuccess(aiChatService.aiChat(objectId, memberId, request));
    }


    /**
     * Ai 대화 세션 조회
     */
    @GetMapping("/{objectId}")
    public ApiResponse<List<ChatSessionResponse>> getChatSession(@PathVariable Long objectId, @InjectMemberId Long memberId){
        return ApiResponse.onSuccess(aiChatService.getChatSession(objectId,memberId));
    }



}
