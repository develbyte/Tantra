package com.tantra.api;

import com.tantra.api.dto.RunRequest;
import com.tantra.api.dto.RunResponse;
import com.tantra.core.service.RunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/v1/runs")
@RequiredArgsConstructor
public class RunController {
    private final RunService runService;

    @PostMapping("/pipelines/{pipelineId}/run")
    public ResponseEntity<RunResponse> triggerRun(
            @PathVariable String pipelineId,
            @RequestBody RunRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        RunResponse response = runService.triggerRun(pipelineId, request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{runId}")
    public ResponseEntity<RunResponse> getRun(@PathVariable String runId) {
        RunResponse response = runService.getRun(runId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{runId}/logs")
    public SseEmitter streamLogs(@PathVariable String runId) {
        return runService.streamLogs(runId);
    }

    @GetMapping
    public ResponseEntity<List<RunResponse>> listRuns(
            @RequestParam(required = false) String pipelineId,
            @RequestParam(required = false) String status) {
        List<RunResponse> runs = runService.listRuns(pipelineId, status);
        return ResponseEntity.ok(runs);
    }
}

