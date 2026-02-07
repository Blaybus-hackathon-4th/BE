package com.example.blaybus4th.domain.aiChat.repository;

import com.example.blaybus4th.domain.aiChat.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    @Query("""
            select distinct cs
              from ChatSession cs
              left join fetch cs.chatMessages cm
             where cs.member.memberId = :memberId
               and cs.object.objectId = :objectId
            """)
    List<ChatSession> findByObjectIdAndMemberId(Long memberId, Long objectId);


}
