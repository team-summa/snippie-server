package com.snippie.backend.summary.domain;

import com.snippie.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@EntityListeners(AuditingEntityListener.class)
@Table(name = "summaries")
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
    @Basic(fetch = FetchType.LAZY)
    private String content;

    @Column(name = "prompt_tokens")
    private Integer promptTokens;

    @Column(name = "completion_tokens")
    private Integer completionTokens;

    @Column(name = "total_tokens")
    private Integer totalTokens;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "summary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TextInput textInput;

    @OneToOne(mappedBy = "summary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CodeDiffInput codeDiffInput;

    @Builder
    private Summary(User user, SummaryType summaryType, String title, String content, Integer promptTokens, Integer completionTokens, Integer totalTokens) {
        this.user = user;
        this.summaryType = summaryType;
        this.title = title;
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public void addTextInput(TextInput textInput) {
        this.textInput = textInput;
        textInput.setSummary(this);
    }

    public void addCodeDiffInput(CodeDiffInput codeDiffInput) {
        this.codeDiffInput = codeDiffInput;
        codeDiffInput.setSummary(this);
    }

    public void changeTitleAndContent(String title, String content) {
        this.title   = title;
        this.content = content;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
