package com.example.blaybus4th.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Institution")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Institution {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long institutionId;

    @Column(nullable = false)
    private String institutionName;

}
