package com.example.blaybus4th.domain.aiChat.service;

import com.example.blaybus4th.domain.aiChat.agent.ObjectHelperAgent;
import com.example.blaybus4th.domain.aiChat.dto.request.AiChatRequest;
import com.example.blaybus4th.domain.aiChat.dto.response.AiChatResponse;
import com.example.blaybus4th.domain.aiChat.repository.AiChatRepository;
import com.example.blaybus4th.domain.member.dto.response.InstitutionsListResponse;
import com.example.blaybus4th.domain.member.entity.Member;
import com.example.blaybus4th.domain.member.repository.MemberRepository;
import com.example.blaybus4th.domain.object.repository.ObjectRepository;
import com.example.blaybus4th.global.apiPayload.code.GeneralErrorCode;
import com.example.blaybus4th.global.apiPayload.exception.GeneralException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiChatService {

    private final AiChatRepository aiChatRepository;
    private final MemberRepository memberRepository;
    private final ObjectRepository objectRepository;
    private final AiServiceRegistry aiServiceRegistry;
    private final ChatMemoryManager chatMemoryManager;
    private final ObjectMapper objectMapper;

    @Transactional
    public AiChatResponse aiChat(Long objectId, Long memberId, AiChatRequest request) throws JsonProcessingException {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND));

        Object object = objectRepository.findById(objectId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.OBJECT_NOT_FOUND));

        ChatMemory chatMemory = chatMemoryManager.memoryOf(memberId);

        String viewStateJson = toJson(request.getViewState());

        ObjectHelperAgent agent = AiServices.builder(ObjectHelperAgent.class)
                .chatModel(aiServiceRegistry.getChatModel())
                .chatMemory(chatMemory)
                .build();

        String rawText = agent.chat(
                request.getUserMessage(),
                viewStateJson
        );

        String cleanJson = sanitizeJsonResponse(rawText);

        return parseResponse(cleanJson);


    }



    private String toJson(Object object) {
        try{
            return objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new GeneralException(GeneralErrorCode.JSON_PROCESSING_ERROR);
        }

    }


    private AiChatResponse parseResponse(String rawJson) {
        try {
            return objectMapper.readValue(rawJson, AiChatResponse.class);
        } catch (Exception e) {
            throw new GeneralException(GeneralErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    private String sanitizeJsonResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            log.error("AI 응답이 비어있습니다.");
            throw new GeneralException(GeneralErrorCode.JSON_PROCESSING_ERROR);
        }
        response = response.replaceAll("```json", "").replaceAll("```", "");
        response = response.trim();
        int startIndex = response.indexOf('{');
        int endIndex = response.lastIndexOf('}');
        if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) {
            log.error("AI 응답에서 유효한 JSON 객체를 찾을 수 없습니다.");
            throw new GeneralException(GeneralErrorCode.JSON_PROCESSING_ERROR);
        }
        String json = response.substring(startIndex, endIndex + 1);


        json = json.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
        json = json.replaceAll("\\}\\]\\s*\"", "}],");
        json = json.replaceAll("\\]\"\\s*,\\s*\"", "],\"");
        json = json.replaceAll("\\}\"\\s*,\\s*\"", "},\"");


        return json;
    }






}

