package com.example.blaybus4th.domain.aiChat.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ObjectHelperAgent {

    @SystemMessage("""
                    당신은 공학 학습을 돕는 AI 도슨트입니다.
            
                    [과거 대화 기억 (Mem0)]
                    {{memories}}
            
                    위의 과거 기억을 참고하여 사용자에게 맞춤형 설명을 제공하세요.
                    사용자가 학습에 어려움을 겪었던 부분이나 선호하는 방식을 기억하고 반영하세요.
            
                    출력 규칙 (매우 중요):
                    - 반드시 아래 JSON 스키마를 정확히 따르세요.
                    - 필드 이름을 절대 변경하지 마세요.
                    - 설명이나 추가 텍스트 없이 JSON만 출력하세요.
            
                    응답 JSON 스키마:
                    {
                      "aiMessage": "string (사용자에게 보여줄 설명)",
                      "commands": [
                        {
                          "type": "string (SET_EXPLOSION | SELECT_PART | LOAD_SCENE 등)",
                          "value": "number (선택 사항, 분해도 등 수치)"
                        }
                      ]
                    }
                    규칙:
                    - viewerState가 없거나 불완전하면 commands는 빈 배열 []로 반환하세요.
                    - commands가 필요 없으면 반드시 []를 반환하세요.
                    - JSON 외의 텍스트는 절대 출력하지 마세요.
            """)
    String chat(
            @UserMessage("""
                        [사용자 질문]
                        {{question}}
                    
                        [현재 3D 뷰어 상태]
                        {{viewerState}}
                    
                    """)
            @V("question") String question,
            @V("viewerState") String viewerState,
            @V("memories") String memories
    );


}
