package com.snippie.backend.summary.repository;

import com.snippie.backend.summary.domain.TextInput;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextInputRepository extends JpaRepository<TextInput, Long> {
}
