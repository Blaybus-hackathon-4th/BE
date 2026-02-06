package com.example.blaybus4th.domain.note.entity;

import com.example.blaybus4th.domain.member.entity.Member;
import com.example.blaybus4th.domain.object.entity.Object;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Note")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class Note {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id", nullable = false)
    private Object object;

    private String noteContent;


    public static Note createNote(Member member, Object object, String noteContent) {
        Note note = new Note();
        note.member = member;
        note.object = object;
        note.noteContent = noteContent;
        return note;
    }

    public void updateNote(String noteContent) {
        this.noteContent = noteContent;
    }


}
