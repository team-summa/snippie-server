package com.snippie.backend.summary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "text_input")
@Getter
@Setter
public class TextInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "summary_id", nullable = false)
    private Summary summary;

    @Lob
    @Column(name = "input_text", nullable = false)
    private String inputText;
}
