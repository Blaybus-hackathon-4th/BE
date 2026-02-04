package com.example.blaybus4th.object.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Object")
@Getter
public class Object {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long objectId;

    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @Column(name = "object_name_kr")
    private String objectNameKr;

    @Column(nullable = false, name = "object_name_en")
    private String objectNameEn;

    @Column(nullable = false)
    private String objectDescription;

    @Column(nullable = false)
    private String objectCategory;

    @Column(nullable = false)
    private String objectThumbnail;

    @Column(nullable = false, name = "is_visited", columnDefinition = "boolean default false")
    private boolean isVisited;



}
