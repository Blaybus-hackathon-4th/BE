package com.example.blaybus4th.domain.object.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Model")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class Model {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id", nullable = false)
    private Object object;

    @Column(nullable = false,name = "model_url")
    private String modelURL;

    @Column(nullable = false,name = "model_name_kr")
    private String modelNameKr;

    @Column(nullable = false,name = "model_name_en")
    private String modelNameEn;

    @Column(nullable = false,name = "model_content",columnDefinition = "text")
    private String modelContent;

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float posX; // 초기 위치

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float posY;

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float posZ;

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float rotX; // 초기 회전

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float rotY;

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float rotZ;

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float rotW;

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float scaleX; // 초기 스케일

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float scaleY;

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float scaleZ;

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float explodeDx; // 분해 방향 벡터

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float explodeDy;

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float explodeDz;

    @Column(nullable = false,columnDefinition = "FLOAT")
    private Float explodeDistance; // 최대 이동 거리

    @Column(nullable = false, columnDefinition = "FLOAT")
    private Float camPosX;

    @Column(nullable = false, columnDefinition = "FLOAT")
    private Float camPosY;

    @Column(nullable = false, columnDefinition = "FLOAT")
    private Float camPosZ;

    @Column(nullable = false, columnDefinition = "FLOAT")
    private Float camTargetX;

    @Column(nullable = false, columnDefinition = "FLOAT")
    private Float camTargetY;

    @Column(nullable = false, columnDefinition = "FLOAT")
    private Float camTargetZ;

    @Column(nullable = false, columnDefinition = "FLOAT")
    private Float camFov;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ModelComponents> modelComponents = new ArrayList<>();
}
