package com.example.blaybus4th.domain.pdf.service;


import com.example.blaybus4th.domain.aiChat.entity.ChatSession;
import com.example.blaybus4th.domain.aiChat.repository.ChatSessionRepository;
import com.example.blaybus4th.domain.aiChat.service.AiServiceRegistry;
import com.example.blaybus4th.domain.aiChat.service.ChatMemoryManager;
import com.example.blaybus4th.domain.member.entity.Member;
import com.example.blaybus4th.domain.member.repository.MemberRepository;
import com.example.blaybus4th.domain.note.entity.Note;
import com.example.blaybus4th.domain.note.repository.NoteRepository;
import com.example.blaybus4th.domain.object.entity.Model;
import com.example.blaybus4th.domain.object.entity.ObjectDetailDescription;
import com.example.blaybus4th.domain.object.entity.OperationPrinciple;
import com.example.blaybus4th.domain.object.entity.StructuralFeature;
import com.example.blaybus4th.domain.object.repository.ModelRepository;
import com.example.blaybus4th.domain.object.repository.ObjectRepository;
import com.example.blaybus4th.domain.pdf.agent.PdfAgent;
import com.example.blaybus4th.domain.pdf.dto.response.PdfResponse;
import com.example.blaybus4th.global.apiPayload.code.GeneralErrorCode;
import com.example.blaybus4th.global.apiPayload.exception.GeneralException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PdfService {

    private final ObjectRepository objectRepository;
    private final MemberRepository memberRepository;
    private final NoteRepository noteRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMemoryManager chatMemoryManager;
    private final AiServiceRegistry aiServiceRegistry;
    private final ModelRepository modelRepository;
    private final ObjectMapper objectMapper;

    public PdfResponse createPdf(Long objectId, Long memberId,String intent) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        com.example.blaybus4th.domain.object.entity.Object object = objectRepository.findWithAllDetails(objectId)
                .orElseThrow(() -> new IllegalArgumentException("Object not found"));

        List<Note> notes = noteRepository.findByMemberIdAndObjectId(memberId, objectId)
                .orElseThrow(() -> new IllegalArgumentException("Notes not found"));
        List<Model> selectedModel = modelRepository.findAllByObjectId(objectId);
        List<ChatSession> chatSession = chatSessionRepository.findByObjectIdAndMemberId(memberId, objectId);

        String objectContext = generateObjectContext(object);
        String structuralContext = structuralContext(object);
        String principleContext = principleContext(object);
        String modelContext = generateModelContext(selectedModel);

        String userNotes = notes.stream()
                .map(Note::getNoteContent)
                .filter(content -> content != null && !content.isBlank())
                .map(content -> "- " + content.trim())
                .collect(Collectors.joining("\n"));
        String chatHistory = chatSession.stream()
                .flatMap(session -> session.getChatMessages().stream())
                .map(msg -> msg.getSenderType() + ": " + msg.getChatContent())
                .collect(Collectors.joining("\n"));

        ChatMemory chatMemory = chatMemoryManager.memoryOf(memberId);

        PdfAgent agent = AiServices.builder(PdfAgent.class)
                .chatModel(aiServiceRegistry.getChatModel())
                .chatMemory(chatMemory)
                .build();

        String rawResponse = agent.chat(
                intent,
                userNotes,
                chatHistory,
                objectContext,
                structuralContext,
                principleContext,
                modelContext
        );
        String cleanJson = sanitizeJsonResponse(rawResponse);
        PdfResponse response = parseResponse(cleanJson);
        return PdfResponse.from(response);

    }



    private String generateModelContext(List<Model> model) {
        if (model == null || model.isEmpty()) {
            return "선택된 부품 정보가 없습니다.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("부품의 정보는 다음과 같습니다:\n");
        for (Model models : model) {
            sb.append("========================================\n");
            sb.append("부품id: ").append(models.getModelId()).append("\n");
            sb.append("부품의 영문명: ").append(models.getModelNameEn()).append("\n");
            sb.append("부품의 한글명: ").append(models.getModelNameKr()).append("\n");
            sb.append("부품 설명: ").append(models.getModelContent()).append("\n");
        }
        return sb.toString();
    }

    private String generateObjectContext(com.example.blaybus4th.domain.object.entity.Object object){
        if (object == null) {
            return "선택된 오브젝트 정보가 없습니다.";
        }
        return "오브젝트 정보는 다음과 같습니다:\n" +
                "오브젝트id: " + object.getObjectId() + "\n" +
                "오브젝트의 영문명: " + object.getObjectNameEn() + "\n" +
                "오브젝트의 한글명: " + object.getObjectNameKr() + "\n" +
                "오브젝트 설명: " + object.getObjectDescription() + "\n";
    }


    private String structuralContext(com.example.blaybus4th.domain.object.entity.Object object) {
        ObjectDetailDescription detail = object.getDetailDescriptions();

        if (detail == null || detail.getStructuralFeatures().isEmpty()) {
            return "등록된 구조적 특징 정보가 없습니다.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("모델의 구조적 특징:\n");
        for (StructuralFeature feature : detail.getStructuralFeatures()) {
            sb.append("- ")
                    .append(feature.getStructuralFeatureDescription())
                    .append("\n");
        }

        return sb.toString();
    }

    private String principleContext(com.example.blaybus4th.domain.object.entity.Object object) {
        ObjectDetailDescription detail = object.getDetailDescriptions();
        if (detail == null || detail.getOperationPrinciples().isEmpty()) {
            return "모델의 작동원리 정보가 없습니다.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("모델의 작동 원리 : ");
        detail.getOperationPrinciples().stream()
                .sorted(Comparator.comparing(OperationPrinciple::getOperationPrincipleOrder))
                .forEach(principle -> {
                    sb.append(principle.getOperationPrincipleOrder())
                            .append(". ")
                            .append(principle.getOperationPrincipleDescription())
                            .append("\n");
                });
        return sb.toString();
    }

    private String buildUnifiedModelContext(
            com.example.blaybus4th.domain.object.entity.Object object,
            List<Model> models
    ) {
        StringBuilder sb = new StringBuilder();

        sb.append("===== OBJECT INFORMATION =====\n");
        sb.append(generateObjectContext(object)).append("\n");

        sb.append("===== MODEL PARTS INFORMATION =====\n");
        sb.append(generateModelContext(models)).append("\n");

        sb.append("===== STRUCTURAL CHARACTERISTICS =====\n");
        sb.append(structuralContext(object)).append("\n");

        sb.append("===== ENGINEERING PRINCIPLES =====\n");
        sb.append(principleContext(object)).append("\n");

        return sb.toString();
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
        json = json.replaceAll("}]\\s*\"", "],");
        json = json.replaceAll("]\"\\s*,\\s*\"", "],\"");
        json = json.replaceAll("}\"\\s*,\\s*\"", "},\"");


        return json;
    }
    private PdfResponse parseResponse(String rawJson) {
        try {
            return objectMapper.readValue(rawJson, PdfResponse.class);
        } catch (Exception e) {
            throw new GeneralException(GeneralErrorCode.JSON_PROCESSING_ERROR);
        }
    }

}
