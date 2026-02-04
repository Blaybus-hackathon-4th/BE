package com.example.blaybus4th.note.entity;

import com.example.blaybus4th.member.entity.Member;
import com.example.blaybus4th.object.entity.Object;
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

}
