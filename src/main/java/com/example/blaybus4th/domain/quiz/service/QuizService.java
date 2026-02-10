package com.example.blaybus4th.domain.quiz.service;

import com.example.blaybus4th.domain.aiChat.service.AiServiceRegistry;
import com.example.blaybus4th.domain.object.entity.*;
import com.example.blaybus4th.domain.object.entity.Object;
import com.example.blaybus4th.domain.object.repository.ModelRepository;
import com.example.blaybus4th.domain.object.repository.ObjectRepository;
import com.example.blaybus4th.domain.quiz.agent.QuizAgent;
import com.example.blaybus4th.domain.quiz.dto.QuizRequestSTO;
import com.example.blaybus4th.domain.quiz.dto.QuizResponseDTO;
import com.example.blaybus4th.domain.quiz.mapper.QuizPromptMapper;
import com.example.blaybus4th.global.apiPayload.code.GeneralErrorCode;
import com.example.blaybus4th.global.apiPayload.exception.GeneralException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.blaybus4th.domain.quiz.converter.QuizConverter.*;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final ModelRepository modelRepository;
    private final ObjectRepository objectRepository;
    private final ObjectMapper objectMapper;
    private final AiServiceRegistry aiServiceRegistry;

    public QuizResponseDTO.createQuizDTO generateQuiz(QuizRequestSTO.ModelInteractionRequestDTO request) {

        QuizResponseDTO.ObjectInfoDTO info = createQuizInfo(request);

        String modelContext = QuizPromptMapper.toModelContext(info);
        String candidateParts = QuizPromptMapper.toCandidateParts(info);
        String chatHistory = QuizPromptMapper.toChatHistory(request.getChatHistory());

        QuizAgent agent = AiServices.builder(QuizAgent.class)
                .chatModel(aiServiceRegistry.getChatModel())
                .build();

        String userMessage = generateUserMessage(info);
        if (userMessage == null || userMessage.isBlank()) {
            throw new GeneralException(GeneralErrorCode._BAD_REQUEST);
        }

        String rawJson = agent.generate(
                info.getObjectName(),               // modelName
                info.getComponentInfos().size(),    // partCount (int)
                modelContext,                       // modelContext
                candidateParts,                     // candidateParts
                chatHistory                         // chatHistory
        );

        return getCreateQuizDTO(rawJson);
    }

    private QuizResponseDTO.createQuizDTO getCreateQuizDTO(String rawJson) {
        return parseQuiz(rawJson);
    }

    private String generateUserMessage(QuizResponseDTO.ObjectInfoDTO info) {
        return "다음 객체에 대한 퀴즈를 만들어 주세요: " + info.getObjectName();
    }

    private QuizResponseDTO.createQuizDTO parseQuiz(String json) {
        try {
            json = json.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();
            return objectMapper.readValue(json, QuizResponseDTO.createQuizDTO.class);
        } catch (Exception e) {
            System.err.println("Failed JSON: " + json);
            throw new GeneralException(
                    GeneralErrorCode.JSON_PROCESSING_ERROR
            );
        }
    }


    public QuizResponseDTO.ObjectInfoDTO createQuizInfo(QuizRequestSTO.ModelInteractionRequestDTO request) {

        Object currentObject = objectRepository.findById(request.getCurrentModelId())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.MODEL_NOT_FOUND));
        ObjectDetailDescription objectDetailDescription = currentObject.getDetailDescriptions();

        List<Model> models = modelRepository.findAllByIdsWithComponents(request.getInteractedPartId());

        List<QuizResponseDTO.ModelInfoDTO> modelInfoDTOS = new ArrayList<>();
        for (Model model : models) {
            List<ModelComponents> modelComponents = model.getModelComponents();

            List<QuizResponseDTO.ModelComponentDTO> modelComponentDTOS = new ArrayList<>();
            for (ModelComponents modelComponent : modelComponents) {
                QuizResponseDTO.ModelComponentDTO componentDTO = toModelComponentDTO(modelComponent);
                modelComponentDTOS.add(componentDTO);
            }

            QuizResponseDTO.ModelInfoDTO modelInfoDTO = toModelInfoDTO(model, modelComponentDTOS);
            modelInfoDTOS.add(modelInfoDTO);
        }

        List<String> principle = objectDetailDescription.getOperationPrinciples()
                .stream()
                .map(OperationPrinciple::getOperationPrincipleDescription)
                .toList();
        List<String> structuralAdvantages = objectDetailDescription.getAdvantages()
                .stream()
                .map(Advantage::getAdvantageDescription)
                .toList();
        List<String> designConstraints = objectDetailDescription.getDisadvantages()
                .stream()
                .map(Disadvantage::getDisadvantageDescription)
                .toList();

        QuizResponseDTO.ObjectInfoDTO result = toObjectInfoDTO(
                currentObject,
                objectDetailDescription,
                principle,
                designConstraints,
                structuralAdvantages,
                modelInfoDTOS
        );

        return result;
    }

}
