package com.tantra.core.repository;

import com.tantra.core.domain.PipelineVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PipelineVersionRepository extends JpaRepository<PipelineVersion, String> {
    List<PipelineVersion> findByPipelineIdOrderByVersionDesc(String pipelineId);
    Optional<PipelineVersion> findByPipelineIdAndVersion(String pipelineId, Integer version);
    Optional<PipelineVersion> findByGitCommitSha(String gitCommitSha);
}

