package com.snippie.backend.summary.repository;

import com.snippie.backend.summary.domain.CodeDiffInput;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeDiffInputRepository extends JpaRepository<CodeDiffInput, Long> {
}
