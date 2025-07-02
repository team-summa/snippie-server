package com.snippie.backend.summary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "code_diff")
@Getter
@Setter
public class CodeDiffInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "summary_id", nullable = false)
    private Summary summary;

    @Lob
    @Column(name = "before_code", nullable = false)
    private String beforeCode;

    @Lob
    @Column(name = "after_code", nullable = false)
    private String afterCode;
}
