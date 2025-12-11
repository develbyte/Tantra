package com.tantra.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tantra.api.dto.RunRequest;
import com.tantra.api.dto.RunResponse;
import com.tantra.core.domain.Pipeline;
import com.tantra.core.domain.Run;
import com.tantra.core.domain.User;
import com.tantra.core.repository.PipelineRepository;
import com.tantra.core.repository.RunRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RunService {
    private final RunRepository runRepository;
    private final PipelineRepository pipelineRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public RunResponse triggerRun(String pipelineId, RunRequest request, String userId) {
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new RuntimeException("Pipeline not found: " + pipelineId));

        Run run = new Run();
        run.setRunId(UUID.randomUUID().toString());
        run.setPipeline(pipeline);
        run.setVersion(pipeline.getVersion());
        run.setStatus(Run.RunStatus.PENDING);

        User user = new User();
        user.setId(userId);
        run.setTriggeredBy(user);

        if (request.getParams() != null) {
            try {
                run.setParamsJson(objectMapper.writeValueAsString(request.getParams()));
            } catch (Exception e) {
                log.error("Failed to serialize run params", e);
            }
        }

        run = runRepository.save(run);
        
        // TODO: Trigger actual execution via executor service
        log.info("Run {} created for pipeline {}", run.getRunId(), pipelineId);

        return toResponse(run);
    }

    public RunResponse getRun(String runId) {
        Run run = runRepository.findById(runId)
                .orElseThrow(() -> new RuntimeException("Run not found: " + runId));
        return toResponse(run);
    }

    public List<RunResponse> listRuns(String pipelineId, String status) {
        List<Run> runs;
        if (pipelineId != null) {
            runs = runRepository.findByPipelineIdOrderByCreatedAtDesc(pipelineId);
        } else if (status != null) {
            runs = runRepository.findByStatus(Run.RunStatus.valueOf(status.toUpperCase()));
        } else {
            runs = runRepository.findAll();
        }
        return runs.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public SseEmitter streamLogs(String runId) {
        SseEmitter emitter = new SseEmitter(30000L);
        // TODO: Implement log streaming
        return emitter;
    }

    private RunResponse toResponse(Run run) {
        RunResponse response = new RunResponse();
        response.setRunId(run.getRunId());
        response.setPipelineId(run.getPipeline().getId());
        response.setVersion(run.getVersion());
        response.setStatus(run.getStatus().name());
        response.setStartedAt(run.getStartedAt());
        response.setCompletedAt(run.getCompletedAt());
        if (run.getTriggeredBy() != null) {
            response.setTriggeredBy(run.getTriggeredBy().getEmail());
        }
        return response;
    }
}

