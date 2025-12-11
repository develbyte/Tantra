package com.tantra.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tantra.api.dto.PipelineCreateRequest;
import com.tantra.api.dto.PipelineResponse;
import com.tantra.api.dto.PipelineSpecDto;
import com.tantra.core.domain.Pipeline;
import com.tantra.core.domain.PipelineVersion;
import com.tantra.core.domain.User;
import com.tantra.core.repository.PipelineRepository;
import com.tantra.core.repository.PipelineVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PipelineService {
    private final PipelineRepository pipelineRepository;
    private final PipelineVersionRepository versionRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public PipelineResponse createPipeline(PipelineCreateRequest request, String userId) {
        Pipeline pipeline = new Pipeline();
        pipeline.setId(UUID.randomUUID().toString());
        pipeline.setName(request.getName());
        pipeline.setVersion(1);

        try {
            String specJson = objectMapper.writeValueAsString(request.getSpec());
            pipeline.setSpecJson(specJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize pipeline spec", e);
        }

        User user = new User();
        user.setId(userId);
        pipeline.setCreatedBy(user);

        pipeline = pipelineRepository.save(pipeline);

        // Create initial version
        createVersion(pipeline, userId);

        return toResponse(pipeline);
    }

    public PipelineResponse getPipeline(String id) {
        Pipeline pipeline = pipelineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pipeline not found: " + id));

        PipelineResponse response = toResponse(pipeline);
        
        // Load versions
        List<PipelineVersionResponse> versions = versionRepository
                .findByPipelineIdOrderByVersionDesc(id)
                .stream()
                .map(this::toVersionResponse)
                .collect(Collectors.toList());
        response.setVersions(versions);

        return response;
    }

    @Transactional
    public PipelineResponse updatePipeline(String id, PipelineCreateRequest request, String userId) {
        Pipeline pipeline = pipelineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pipeline not found: " + id));

        pipeline.setName(request.getName());
        pipeline.setVersion(pipeline.getVersion() + 1);

        try {
            String specJson = objectMapper.writeValueAsString(request.getSpec());
            pipeline.setSpecJson(specJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize pipeline spec", e);
        }

        pipeline = pipelineRepository.save(pipeline);
        createVersion(pipeline, userId);

        return toResponse(pipeline);
    }

    @Transactional
    public void deletePipeline(String id) {
        pipelineRepository.deleteById(id);
    }

    private PipelineVersion createVersion(Pipeline pipeline, String userId) {
        PipelineVersion version = new PipelineVersion();
        version.setId(UUID.randomUUID().toString());
        version.setPipeline(pipeline);
        version.setVersion(pipeline.getVersion());
        version.setSpecJson(pipeline.getSpecJson());

        User user = new User();
        user.setId(userId);
        version.setCreatedBy(user);

        return versionRepository.save(version);
    }

    private PipelineResponse toResponse(Pipeline pipeline) {
        PipelineResponse response = new PipelineResponse();
        response.setId(pipeline.getId());
        response.setName(pipeline.getName());
        response.setVersion(pipeline.getVersion());
        response.setCreatedAt(pipeline.getCreatedAt());
        response.setUpdatedAt(pipeline.getUpdatedAt());

        if (pipeline.getCreatedBy() != null) {
            response.setCreatedBy(pipeline.getCreatedBy().getEmail());
        }

        try {
            PipelineSpecDto spec = objectMapper.readValue(
                    pipeline.getSpecJson(),
                    PipelineSpecDto.class
            );
            response.setSpec(spec);
        } catch (Exception e) {
            log.error("Failed to deserialize pipeline spec", e);
        }

        return response;
    }

    private PipelineVersionResponse toVersionResponse(PipelineVersion version) {
        PipelineVersionResponse response = new PipelineVersionResponse();
        response.setId(version.getId());
        response.setVersion(version.getVersion());
        response.setGitCommitSha(version.getGitCommitSha());
        response.setCreatedAt(version.getCreatedAt());

        if (version.getCreatedBy() != null) {
            response.setCreatedBy(version.getCreatedBy().getEmail());
        }

        return response;
    }
}

