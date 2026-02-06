package com.example.blaybus4th.domain.note.repository;

import com.example.blaybus4th.domain.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("""
            select n
              from Note n
             where n.member.memberId = :memberId
               and n.object.objectId = :objectId
            """)
    Optional<List<Note>> findByMemberIdAndObjectId(Long memberId, Long objectId);


}
