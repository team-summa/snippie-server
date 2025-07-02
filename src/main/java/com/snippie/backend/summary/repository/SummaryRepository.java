package com.snippie.backend.summary.repository;

import com.snippie.backend.summary.domain.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
}
