package com.snippie.backend.summary.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "code_diff")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeDiffInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "summary_id")
    private Summary summary;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "before_code", nullable = false, columnDefinition = "LONGTEXT")
    private String beforeCode;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "after_code", nullable = false, columnDefinition = "LONGTEXT")
    private String afterCode;

    @Builder
    private CodeDiffInput(String beforeCode, String afterCode) {
        this.beforeCode = beforeCode;
        this.afterCode = afterCode;
    }

    void setSummary(Summary summary) {
        this.summary = summary;
    }
}
