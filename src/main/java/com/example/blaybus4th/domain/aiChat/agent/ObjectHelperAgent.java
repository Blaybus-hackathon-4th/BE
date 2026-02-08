package com.example.blaybus4th.domain.aiChat.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ObjectHelperAgent {

    @SystemMessage("""
                    당신은 공학 학습을 돕는 AI 도슨트입니다.
            
                    [과거 대화 기억 (Mem0)]
                    {{memories}}
            
                    [사용자가 보고있는 오브젝트의 정보]
                    오브젝트 이름 : {{objectName}}
                    오브젝트 설명 : {{objectDescription}}
            
                    [공학 원리]
                    {{engineeringPrinciple}}
            
                    [구조적 특징]
                    {{structuralCharacteristics}}
            
                    위의 과거 기억을 참고하여 사용자에게 맞춤형 설명을 제공하세요.
                    사용자가 학습에 어려움을 겪었던 부분이나 선호하는 방식을 기억하고 반영하세요.
            
                    출력 규칙 (매우 중요):
                    - 반드시 아래 JSON 스키마를 정확히 따르세요.
                    - 필드 이름을 절대 변경하지 마세요.
                    - 설명이나 추가 텍스트 없이 JSON만 출력하세요.
            
                    [최종 출력 예시]
            
                    {
                      "thought": "사용자가 해당 모델에 대한 자세한 설명을 요구.",
                      "aiMessage": "string (사용자에게 보여줄 설명 최대 700자 이내)",
                      "chatSessionTitle" : "string (질문 기반 요약 제목 생성(최대 15자))",
                    }
                    규칙:
                    - JSON 외의 텍스트는 절대 출력하지 마세요.
            """)
    String chat(
            @UserMessage("""
                        [사용자 질문]
                        {{question}}
                    """)
            @V("question") String question,
//            @V("viewerState") String viewerState,
            @V("memories") String memories,
            @V("engineeringPrinciple") String engineeringPrinciple,
            @V("structuralCharacteristics") String structuralCharacteristics,
            @V("objectName") String objectName,
            @V("objectDescription") String objectDescription
    );


}
