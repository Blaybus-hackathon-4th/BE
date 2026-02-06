package com.example.blaybus4th.domain.object.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Disadvantage")
@Getter
@NoArgsConstructor( access = AccessLevel.PROTECTED)
public class Disadvantage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disadvantage_id")
    private Long disadvantageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_detail_id", nullable = false)
    private ObjectDetailDescription objectDetailDescription;

    @Column(nullable = false, name = "disadvantage_description")
    private String disadvantageDescription;


}
