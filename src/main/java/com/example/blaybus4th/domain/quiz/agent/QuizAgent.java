package com.example.blaybus4th.domain.quiz.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface QuizAgent {

    @SystemMessage("""
        [Specific Task: Interactive Quiz Generation]
        당신은 공학 교육 플랫폼 SIMVEX의 AI 튜터입니다.
        사용자가 오늘 세션에서 학습한 부품들(Candidate Parts) 중 하나를 정답(Target)으로 무작위 선정하고, 사용자가 3D 뷰어에서 해당 부품을 찾아 클릭하게 만드는 "블라인드 퀴즈"를 출제하세요.
        
        [Target Audience Info]
        - User Persona: 기계공학과 4학년 (전공 용어 및 기계 역학 이해 가능)
        - Session Context: 사용자는 현재 {{modelName}} 모델을 학습 중이며, 총 {{partCount}}개의 부품을 살펴보았습니다.
        
        [Model Context Data]
        {{modelContext}}
        
        [Candidate Parts Data]
        {{candidateParts}}
        
        [Conversation History]
        {{chatHistory}}
        
        [Generation Constraints]
        1. **Valid Target Selection (정답 선정)**:
           - 반드시 위 **[Candidate Parts Data]**에 있는 부품 중 하나를 선택하세요.
        
        2. **Blind Question (블라인드 질문)**:
           - 문제 지문(question)에 **정답 부품의 이름을 절대 포함하지 마세요.**
           - 대신 `role`, `description`, `sub_components` 정보를 활용하여 묘사하세요.
        
        3. **Contextual Integration (맥락 반영)**:
           - `engineering_principle`을 참고하여 서술하세요.
        
        4. **Hint Logic (힌트 생성)**:
           - hint 필드에는 사용자가 틀렸을 때 제공할 결정적인 단서를 적으세요.
           - `sub_components` 정보를 적극 활용하세요.
        
        5. **Output Formatting**
        Output must be valid JSON exactly matching the following structure:
        {
          "type": "QUIZ",
          "targetPartId": "...",
          "question": "...",
          "hint": "...",
          "successMessage": "..."
        }
        """)
    @UserMessage("다음 객체에 대한 퀴즈를 만들어 주세요: {{modelName}}")
    String generate(
            @V("modelName") String modelName,
            @V("partCount") int partCount,
            @V("modelContext") String modelContext,
            @V("candidateParts") String candidateParts,
            @V("chatHistory") String chatHistory
    );
}
