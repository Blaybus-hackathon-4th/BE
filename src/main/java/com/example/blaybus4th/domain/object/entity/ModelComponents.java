package com.example.blaybus4th.domain.object.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ModelComponents")
@Getter
@NoArgsConstructor( access = lombok.AccessLevel.PROTECTED)
public class ModelComponents {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelComponentsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @Column(nullable = false)
    private String modelComponentsName;

    @Column(nullable = false)
    private String modelComponentsContent;


}
