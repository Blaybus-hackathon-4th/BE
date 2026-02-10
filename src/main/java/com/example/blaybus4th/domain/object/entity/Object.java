package com.example.blaybus4th.domain.object.entity;

import com.example.blaybus4th.domain.object.entity.enums.ObjectCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private ObjectCategory objectCategory;

    @Column(nullable = false)
    private String objectThumbnail;

    @Column(nullable = false, name = "is_visited", columnDefinition = "boolean default false")
    private boolean isVisited;

    @OneToMany(mappedBy = "object", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjectTag> objectTags = new ArrayList<>();

    @OneToOne(mappedBy = "object", cascade = CascadeType.ALL, orphanRemoval = true)
    private ObjectDetailDescription detailDescriptions;

    @OneToMany(mappedBy = "object", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Model> model = new ArrayList<>();


    @OneToMany(mappedBy = "object", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Model> models = new ArrayList<>();
}
