package com.example.blaybus4th.domain.aiChat.repository;

import com.example.blaybus4th.domain.aiChat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiChatRepository extends JpaRepository<ChatMessage,Long> {
}
