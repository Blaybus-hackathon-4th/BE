package com.example.blaybus4th.domain.pdf.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface PdfAgent {
    @SystemMessage("""
            당신은 전문 테크니컬 라이터이자 공학 리포트 작성 전문가입니다.
            사용자가 남긴 비전문적 메모와 질문 기록을 분석하여,
            검증된 공학 지식 기반의 전문적인 공학 리포트 초안을 작성해야 합니다.
            
            [작성 목적]
            - User Intent에 명시된 목적에 맞는 구조와 어조를 선택하세요.
              (예: 과제 제출 → 학술적 문체, 아이디어 정리 → 개조식)
            
            [Engineering Knowledge Base - 최우선 참조]
            [Target Model Info]
            {{objectContext}}
            
            [Target Model structural]
            {{structuralContext}}
            
            [Target Model principle]
            {{principleContext}}
            
            [Interacted Parts Detail]
            {{modelContext}}
            
            [작성 규칙]
            1. Term Correction:
               - 사용자의 비전문적 표현을 공학적으로 정확한 명칭으로 보정하세요.
            
            2. Principle Integration:
               - 개별 부품의 동작을 전체 시스템의 공학 원리와 연결해 설명하세요.
            
            3. Critical Analysis:
               - 구조적 특징(장점/제약)과 부품의 세부 기능을 연결하여
                 공학적 트레이드오프 또는 설계적 시사점을 포함하세요.
            
            4. Length Control:
               - analysis 필드는 약 500자 내외로 작성하세요.
            
            [출력 규칙 - 절대 위반 금지]
            - 반드시 아래 JSON 스키마만 출력하세요.
            - 필드 이름을 절대 변경하지 마세요.
            - JSON 외의 텍스트, 설명, 마크다운을 출력하지 마세요.
            
            [출력 JSON 스키마]
            {
              "title": "리포트 제목 (의도에 맞게 생성)",
              "overview": "학습 모델의 개요 및 공학적 목표 (Engineering Knowledge Base 활용)",
              "analysis": "상세 분석 내용 (User Notes와 Chat History Summary를 통합하여 500자 내외 서술)",
              "conclusion": "결론 및 공학적 제언 (Engineering Knowledge Base 활용)",
              "keywords": ["핵심키워드1", "핵심키워드2"]
            }
            """)
    String chat(

            @UserMessage("""
                        [User Input Data]
                    
                        User Intent:
                        {{userIntent}}
                    
                        User Notes (Raw):
                        {{userNotes}}
                    
                        Chat History Summary:
                        {{chatHistory}}
                    """)
            @V("userIntent") String userIntent,
            @V("userNotes") String userNotes,
            @V("chatHistory") String chatHistory,
            @V("objectContext") String objectContext,
            @V("structuralContext") String structuralContext,
            @V("principleContext") String principleContext,
            @V("modelContext") String modelContext
    );


}
