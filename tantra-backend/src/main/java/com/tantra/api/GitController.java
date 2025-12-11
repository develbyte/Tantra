package com.tantra.api;

import com.tantra.api.dto.GitCommitRequest;
import com.tantra.api.dto.GitDiffResponse;
import com.tantra.api.dto.GitVersionResponse;
import com.tantra.core.service.GitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/git")
@RequiredArgsConstructor
public class GitController {
    private final GitService gitService;

    @PostMapping("/commit")
    public ResponseEntity<GitVersionResponse> commitChanges(
            @RequestBody GitCommitRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        GitVersionResponse response = gitService.commitPipeline(request.getPipelineId(), request.getMessage(), userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/versions/{pipelineId}")
    public ResponseEntity<List<GitVersionResponse>> getVersions(@PathVariable String pipelineId) {
        List<GitVersionResponse> versions = gitService.getVersions(pipelineId);
        return ResponseEntity.ok(versions);
    }

    @PostMapping("/diff")
    public ResponseEntity<GitDiffResponse> getDiff(@RequestBody GitDiffRequest request) {
        GitDiffResponse diff = gitService.getDiff(request.getPipelineId(), request.getFromVersion(), request.getToVersion());
        return ResponseEntity.ok(diff);
    }

    @PostMapping("/rollback")
    public ResponseEntity<GitVersionResponse> rollback(
            @RequestBody GitRollbackRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        GitVersionResponse response = gitService.rollback(request.getPipelineId(), request.getVersion(), userId);
        return ResponseEntity.ok(response);
    }

    public static class GitDiffRequest {
        private String pipelineId;
        private Integer fromVersion;
        private Integer toVersion;
        // Getters and setters
        public String getPipelineId() { return pipelineId; }
        public void setPipelineId(String pipelineId) { this.pipelineId = pipelineId; }
        public Integer getFromVersion() { return fromVersion; }
        public void setFromVersion(Integer fromVersion) { this.fromVersion = fromVersion; }
        public Integer getToVersion() { return toVersion; }
        public void setToVersion(Integer toVersion) { this.toVersion = toVersion; }
    }

    public static class GitRollbackRequest {
        private String pipelineId;
        private Integer version;
        // Getters and setters
        public String getPipelineId() { return pipelineId; }
        public void setPipelineId(String pipelineId) { this.pipelineId = pipelineId; }
        public Integer getVersion() { return version; }
        public void setVersion(Integer version) { this.version = version; }
    }
}

