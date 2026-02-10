package com.example.blaybus4th.domain.aiChat.controller;

import com.example.blaybus4th.domain.aiChat.dto.request.AiChatRequest;
import com.example.blaybus4th.domain.aiChat.dto.request.AiDocentRequest;
import com.example.blaybus4th.domain.aiChat.dto.response.AiChatResponse;
import com.example.blaybus4th.domain.aiChat.dto.response.AiDocentResponse;
import com.example.blaybus4th.domain.aiChat.dto.response.ChatSessionResponse;
import com.example.blaybus4th.domain.aiChat.service.AiChatService;
import com.example.blaybus4th.global.annotation.InjectMemberId;
import com.example.blaybus4th.global.apiPayload.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Ai", description = "AI 대화 요청, AI 도슨트 api")
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;

    /**
     * Ai 대화 요청
     * 담당자 : 김영욱
     */
    @Operation(summary = "AI 대화 요청",description = "일반적인 대화를 요청합니다.")
    @PostMapping("/{objectId}")
    public ApiResponse<AiChatResponse> aiChat(
            @PathVariable Long objectId,
            @InjectMemberId Long memberId,
            @RequestBody AiChatRequest request
    ) throws JsonProcessingException {
        return ApiResponse.onSuccess(aiChatService.aiChat(objectId, memberId, request));
    }


    /**
     * Ai 대화 세션 조회
     */
    @Operation(summary = "AI 대화 세션 조회",description = "특정 오브젝트에 대한 AI 대화 세션을 조회합니다.")
    @GetMapping("/{objectId}")
    public ApiResponse<List<ChatSessionResponse>> getChatSession(@PathVariable Long objectId, @InjectMemberId Long memberId){
        return ApiResponse.onSuccess(aiChatService.getChatSession(objectId,memberId));
    }


    /**
     * Ai 도슨트
     */
    @Operation(summary = "AI 도슨트",description = "도슨트 AI 대화를 요청합니다.")
    @PostMapping("/docent")
    public ApiResponse<AiDocentResponse> aiDocent(@RequestBody AiDocentRequest request, @InjectMemberId Long memberId) throws JsonProcessingException {
        return ApiResponse.onSuccess(aiChatService.aiDocent(request,memberId));
    }



}
