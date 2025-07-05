package com.snippie.backend.summary.repository;

import com.snippie.backend.summary.domain.Summary;
import com.snippie.backend.summary.domain.SummaryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
    Summary getSummaryById(Long id);
    List<Summary> findAllByUserId(Long userid);
    List<Summary> findAllByUserIdAndSummaryType(Long userid, SummaryType summaryType);
}
