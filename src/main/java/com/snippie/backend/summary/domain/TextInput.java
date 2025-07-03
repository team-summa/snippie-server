package com.snippie.backend.summary.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "text_input")
public class TextInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "summary_id")
    private Summary summary;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "input_text", nullable = false, columnDefinition = "LONGTEXT")
    private String inputText;

    @Builder
    private TextInput(String inputText) {
        this.inputText = inputText;
    }

    void setSummary(Summary summary) {
        this.summary = summary;
    }
}
