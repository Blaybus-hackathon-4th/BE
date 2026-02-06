package com.example.blaybus4th.domain.object.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ObjectTag")
@Getter
public class ObjectTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long objectTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Object object;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Tag tag;

    public ObjectTag(Object object, Tag tag) {
        this.object = object;
        this.tag = tag;
    }
}