package com.tantra.core.repository;

import com.tantra.core.domain.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PipelineRepository extends JpaRepository<Pipeline, String> {
    Optional<Pipeline> findByName(String name);
    List<Pipeline> findByCreatedByEmail(String email);
}

