package com.tantra.core.repository;

import com.tantra.core.domain.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunRepository extends JpaRepository<Run, String> {
    List<Run> findByPipelineIdOrderByCreatedAtDesc(String pipelineId);
    List<Run> findByStatus(Run.RunStatus status);
}

