package com.example.blaybus4th.domain.aiChat.service;

import com.example.blaybus4th.domain.aiChat.agent.DocentAgent;
import com.example.blaybus4th.domain.aiChat.agent.ObjectHelperAgent;
import com.example.blaybus4th.domain.aiChat.dto.request.AiChatRequest;
import com.example.blaybus4th.domain.aiChat.dto.request.AiDocentRequest;
import com.example.blaybus4th.domain.aiChat.dto.response.AiChatResponse;
import com.example.blaybus4th.domain.aiChat.dto.response.AiDocentResponse;
import com.example.blaybus4th.domain.aiChat.dto.response.ChatSessionResponse;
import com.example.blaybus4th.domain.aiChat.entity.ChatMessage;
import com.example.blaybus4th.domain.aiChat.entity.ChatSession;
import com.example.blaybus4th.domain.aiChat.enums.CommandType;
import com.example.blaybus4th.domain.aiChat.repository.AiChatRepository;
import com.example.blaybus4th.domain.aiChat.repository.ChatSessionRepository;
import com.example.blaybus4th.domain.aiChat.tool.Mem0Tools;
import com.example.blaybus4th.domain.aiChat.tool.MemberContextHolder;
import com.example.blaybus4th.domain.member.entity.Member;
import com.example.blaybus4th.domain.member.repository.MemberRepository;
import com.example.blaybus4th.domain.object.entity.*;
import com.example.blaybus4th.domain.object.entity.Object;
import com.example.blaybus4th.domain.object.repository.ModelRepository;
import com.example.blaybus4th.domain.object.repository.ObjectDetailDescriptionRepository;
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

import java.util.*;
import java.util.stream.Collectors;

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
    private final Mem0Service mem0Service; // 추가
    private final Mem0Tools mem0Tools;
    private final ChatSessionRepository chatSessionRepository;
    private final ModelRepository modelRepository;
    private final ObjectDetailDescriptionRepository objectDetailDescriptionRepository;

    @Transactional
    public AiChatResponse aiChat(Long objectId, Long memberId, AiChatRequest request) throws JsonProcessingException {

        MemberContextHolder.set(String.valueOf(memberId));

        try {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND));

//            Object object = objectRepository.findById(objectId)
//                    .orElseThrow(() -> new GeneralException(GeneralErrorCode.OBJECT_NOT_FOUND));
            Object object = objectRepository.findWithAllDetails(objectId)
                    .orElseThrow(() -> new GeneralException(GeneralErrorCode.OBJECT_NOT_FOUND));
            ChatSession chatSession;

            if (request.getChatSessionId() == null) {
                chatSession = ChatSession.createChatSession(member, object);
                chatSessionRepository.save(chatSession);
            } else {
                chatSession = chatSessionRepository.findById(request.getChatSessionId())
                        .orElseThrow(() -> new GeneralException(GeneralErrorCode.CHAT_SESSION_NOT_FOUND));
            }

            ChatMessage userMessage = ChatMessage.userMessage(request.getUserMessage());
            chatSession.addMessage(userMessage);

            ChatMemory chatMemory = chatMemoryManager.memoryOf(memberId);

//            String viewStateJson = toJson(request.getViewState());

            String engineeringPrinciple = principleContext(object);

            String structuralCharacteristics = structuralContext(object);

            String mem0Memory = mem0Service.searchMemory(request.getUserMessage());

            String objectName = object.getObjectNameKr() + " (" + object.getObjectNameEn() + ")";

            if (mem0Memory.isEmpty()) {
                mem0Memory = "관련된 과거 기억이 없습니다.";
            }


            ObjectHelperAgent agent = AiServices.builder(ObjectHelperAgent.class)
                    .chatModel(aiServiceRegistry.getChatModel())
                    .chatMemory(chatMemory)
                    .tools(mem0Tools)
                    .build();

            String rawText = agent.chat(
                    request.getUserMessage(),
//                    viewStateJson,
                    mem0Memory,
                    engineeringPrinciple,
                    structuralCharacteristics,
                    objectName,
                    object.getDetailDescriptions().getObjectDetailDescription()

            );

            String cleanJson = sanitizeJsonResponse(rawText);

            AiChatResponse response = parseResponse(cleanJson);

            if (chatSession.getChatSessionTitle().isEmpty()) {
                chatSession.updateTitle(response.getChatSessionTitle());
            }

            ChatMessage aiMessage = ChatMessage.aiMessage(response.getAiMessage());
            chatSession.addMessage(aiMessage);

            return AiChatResponse.from(response, chatSession);

        } finally {
            MemberContextHolder.clear();
        }


    }


    @Transactional
    public List<ChatSessionResponse> getChatSession(Long objectId, Long memberId) {

        List<ChatSession> chatSession = chatSessionRepository.findByObjectIdAndMemberId(memberId, objectId);

        if (chatSession.isEmpty()) {
            throw new GeneralException(GeneralErrorCode.CHAT_SESSION_NOT_FOUND);
        }
        return chatSession.stream()
                .map(ChatSessionResponse::from)
                .toList();

    }


    @Transactional
    public AiDocentResponse aiDocent(AiDocentRequest request, Long memberId) throws JsonProcessingException {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND));

        Object object = objectRepository.findWithAllDetails(request.getObjectId())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.OBJECT_NOT_FOUND));

        Model model = null;
        List<Model> allModels;
        if (request.getModelId() != null) {
            model = modelRepository.findWithComponent(request.getModelId())
                    .orElseThrow(() -> new GeneralException(GeneralErrorCode.MODEL_NOT_FOUND));
            allModels = modelRepository.findAllExcludingCurrentId(request.getModelId());
        } else {
            allModels = modelRepository.findAllByObject(request.getObjectId());
        }
        List<Model> selectedModel = modelRepository.findAllByObjectId(request.getObjectId());

        List<Object> allObjects = objectRepository.findAllExcludingCurrentId(request.getObjectId());


        String allObjectsContext = generateAllObjectsContext(allObjects);

        String objectContext = generateObjectContext(object);

        String allModelsContext = generateAllModelsContext(allModels);


        ChatSession chatSession;
        if (request.getChatSessionId() == null) {
            chatSession = ChatSession.createChatSession(member, object);
            chatSessionRepository.save(chatSession);
        } else {
            chatSession = chatSessionRepository.findById(request.getChatSessionId())
                    .orElseThrow(() -> new GeneralException(GeneralErrorCode.CHAT_SESSION_NOT_FOUND));
        }

        chatSession.addMessage(ChatMessage.userMessage(request.getUserMessage()));

        String componentContext = generateComponentContext(model);

        String modelContext = generateModelContext(selectedModel);

        String engineeringPrinciple = principleContext(object);

        String structuralCharacteristics = structuralContext(object);

        String partId = request.getModelId() != null ?
                String.valueOf(request.getModelId()) : "선택한 부품이 없습니다.";

        String cameraPosition = (request.getViewState() != null && request.getViewState().getCameraPosition() != null)
                ? objectMapper.writeValueAsString(request.getViewState().getCameraPosition())
                : "[0, 0, 0]";

        Double explosionValue = (request.getViewState() != null)
                ? request.getViewState().getExplosionValue()
                : 0.0;

        String mem0Memory = mem0Service.searchMemory(request.getUserMessage());
        if (mem0Memory == null || mem0Memory.isEmpty()) {
            mem0Memory = "관련 기록이 없습니다.";
        }

        ChatMemory chatMemory = chatMemoryManager.memoryOf(memberId);

        DocentAgent agent = AiServices.builder(DocentAgent.class)
                .chatModel(aiServiceRegistry.getChatModel())
                .chatMemory(chatMemory)
                .tools(mem0Tools)
                .build();

        String rawResponse = agent.chat(
                request.getUserMessage(),
                partId,
                mem0Memory,
                modelContext,
                componentContext,
                explosionValue,
                engineeringPrinciple,
                structuralCharacteristics,
                cameraPosition,
                allModelsContext,
                objectContext,
                allObjectsContext
        );

        String cleanJson = sanitizeJsonResponse(rawResponse);
        AiDocentResponse response = objectMapper.readValue(cleanJson, AiDocentResponse.class);
        ChatMessage aiMessage = ChatMessage.aiMessage(response.getAiMessage());
        chatSession.addMessage(aiMessage);

        chatSessionRepository.save(chatSession);

        log.debug("response : {}", response.toString());

        if (response.getCommands() != null && !response.getCommands().isEmpty()) {
            AiDocentResponse.ViewerCommander firstCommand = response.getCommands().getFirst();
            if (firstCommand.getType() == CommandType.LOAD_SCENE) {
                Object targetObject = objectRepository.findById(Long.parseLong(firstCommand.getObjectId()))
                        .orElseThrow(() -> new GeneralException(GeneralErrorCode.OBJECT_NOT_FOUND));
                ChatSession newSession = ChatSession.createChatSession(member, targetObject);
                newSession.updateTitle(targetObject.getObjectNameKr() + " 학습");
                chatSessionRepository.save(newSession);
                String followUpMsg = firstCommand.getFollowUpMessage();
                if (followUpMsg != null && !followUpMsg.trim().isEmpty()) {
                    ChatMessage followUpChatMessage = ChatMessage.aiMessage(followUpMsg);
                    newSession.addMessage(followUpChatMessage);
                    chatSessionRepository.save(newSession);
                }

                return AiDocentResponse.from(response, newSession);
            }
        }


        if (chatSession.getChatSessionTitle().isEmpty()) {
            chatSession.updateTitle(response.getChatSessionTitle());
        }

        return AiDocentResponse.from(response, chatSession);
    }

    private String generateObjectContext(Object object){
        if (object == null) {
            return "선택된 오브젝트 정보가 없습니다.";
        }
        return "오브젝트 정보는 다음과 같습니다:\n" +
                "오브젝트id: " + object.getObjectId() + "\n" +
                "오브젝트의 영문명: " + object.getObjectNameEn() + "\n" +
                "오브젝트의 한글명: " + object.getObjectNameKr() + "\n" +
                "오브젝트 설명: " + object.getObjectDescription() + "\n";
    }


    private String generateAllObjectsContext(List<Object> allObjects) {
        if (allObjects.isEmpty()) {
            return "존재하는 오브젝트가 존재하지않습니다.";
        }
        StringBuilder sb = new StringBuilder();
        for (Object object : allObjects) {
            sb.append("========================================\n");
            sb.append("object ID: ").append(object.getObjectId()).append("\n");
            sb.append("Name: ").append(object.getObjectNameKr())
                    .append(" (").append(object.getObjectNameEn()).append(")\n");
            sb.append("Description: ").append(object.getDetailDescriptions()).append("\n");
            sb.append("\n");
        }
        return sb.toString();
    }

    private String generateAllModelsContext(List<Model> allModels) {
        if (allModels.isEmpty()) {
            return "해당 오브젝트의 부품정보가 없습니다.";
        }
        StringBuilder sb = new StringBuilder();
        for (Model model : allModels) {
            sb.append("========================================\n");
            sb.append("model ID: ").append(model.getModelId()).append("\n");
            sb.append("object ID: ").append(model.getObject().getObjectId()).append("\n");
            sb.append("Name: ").append(model.getModelNameKr())
                    .append(" (").append(model.getModelNameEn()).append(")\n");
            sb.append("Description: ").append(model.getModelContent()).append("\n");
            sb.append("Components List:\n");
            if (model.getModelComponents().isEmpty()) {
                sb.append("(등록된 구성부품 없음)\n");
            } else {
                for (ModelComponents c : model.getModelComponents()) {
                    sb.append("   - [ID:").append(c.getModelComponentsId()).append("] ")
                            .append(c.getModelComponentsName())
                            .append(": ")
                            .append(c.getModelComponentsContent())
                            .append("\n");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    private String structuralContext(Object object) {
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

    private String principleContext(Object object) {
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


    private String generateComponentContext(Model model) {
        if (model == null) {
            return "선택된 부품이 없습니다. 제공된 오브젝트를 위주로 설명하세요";
        }
        List<ModelComponents> modelComponents = model.getModelComponents();
        if (modelComponents == null || modelComponents.isEmpty()) {
            return "등록된 부품 정보가 없습니다. 제공된 오브젝트를 위주로 설명하세요";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("이 오브젝트의 구성부품들은 다음과 같습니다:\n");
        sb.append("아래에 표시되는 '부품 id'는 구성부품(ModelComponents) 자체의 id가 아니라, 해당 구성부품이 속한 모델(Model)의 id(modelId)입니다.\n");
        for (ModelComponents component : modelComponents) {
            sb.append("- 부품 id: ").append(component.getModel().getModelId()).append("\n");
            sb.append(" 부품명: ").append(component.getModelComponentsName()).append("\n");
            sb.append(" 설명: ").append(component.getModelComponentsContent()).append("\n\n");
        }
        return sb.toString();
    }


    private String toJson(java.lang.Object object) {
        try {
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
        json = json.replaceAll("}]\\s*\"", "],");
        json = json.replaceAll("]\"\\s*,\\s*\"", "],\"");
        json = json.replaceAll("}\"\\s*,\\s*\"", "},\"");


        return json;
    }


}
