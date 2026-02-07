package com.example.blaybus4th.domain.aiChat.service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatMemoryManager {

    private final Map<Long, ChatMemory> memoryStore = new ConcurrentHashMap<>();


    public ChatMemory memoryOf(Long memberId){
        return memoryStore.computeIfAbsent(
                memberId,
                id -> MessageWindowChatMemory.withMaxMessages(10)
        );
    }


    public void clearMemory(Long memberId){
        memoryStore.remove(memberId);
    }


}
