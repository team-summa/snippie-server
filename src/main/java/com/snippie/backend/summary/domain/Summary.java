package com.snippie.backend.summary.domain;

import com.snippie.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "summaries")
@Getter
@Setter
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SummaryType summaryType;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "summary", cascade = CascadeType.ALL)
    private TextInput textInput;

    @OneToOne(mappedBy = "summary", cascade = CascadeType.ALL)
    private CodeDiffInput codeDiffInput;
}
