package com.example.blaybus4th.domain.aiChat.tool;


import com.example.blaybus4th.domain.aiChat.service.Mem0Service;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mem0Tools {

    private final Mem0Service mem0Service;


    @Tool("사용자의 학습 성향, 관심사, 기본 정보 등 장기적으로 기억해야할 중요한 정보를 저장합니다.")
    public void saveMemberPreference(String text){
        mem0Service.addMemory(text);
    }


}
