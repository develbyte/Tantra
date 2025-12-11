package com.tantra.core.service;

import com.tantra.api.dto.GitDiffResponse;
import com.tantra.api.dto.GitVersionResponse;
import com.tantra.core.domain.Pipeline;
import com.tantra.core.domain.PipelineVersion;
import com.tantra.core.repository.PipelineRepository;
import com.tantra.core.repository.PipelineVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitService {
    private final PipelineRepository pipelineRepository;
    private final PipelineVersionRepository versionRepository;

    @Value("${tantra.git.base-path:./tantra-repos}")
    private String gitBasePath;

    public GitVersionResponse commitPipeline(String pipelineId, String message, String userId) {
        try {
            Pipeline pipeline = pipelineRepository.findById(pipelineId)
                    .orElseThrow(() -> new RuntimeException("Pipeline not found: " + pipelineId));

            Path repoPath = getOrCreateRepo(pipelineId);
            Git git = Git.open(repoPath.toFile());

            // Write pipeline spec to file
            Path specFile = repoPath.resolve("pipeline.json");
            Files.writeString(specFile, pipeline.getSpecJson());

            // Stage and commit
            git.add().addFilepattern("pipeline.json").call();
            org.eclipse.jgit.api.CommitCommand commit = git.commit();
            if (message != null && !message.isEmpty()) {
                commit.setMessage(message);
            } else {
                commit.setMessage("Update pipeline: " + pipeline.getName());
            }
            commit.setAuthor("Tantra User", userId + "@tantra.local").call();

            String commitSha = git.getRepository().resolve("HEAD").getName();
            
            // Update version with commit SHA
            PipelineVersion version = versionRepository
                    .findByPipelineIdAndVersion(pipelineId, pipeline.getVersion())
                    .orElseThrow();
            version.setGitCommitSha(commitSha);
            versionRepository.save(version);

            git.close();

            GitVersionResponse response = new GitVersionResponse();
            response.setCommitSha(commitSha);
            response.setMessage(message);
            response.setAuthor(userId);
            response.setVersion(pipeline.getVersion());
            response.setTimestamp(LocalDateTime.now());

            return response;
        } catch (Exception e) {
            log.error("Failed to commit pipeline to git", e);
            throw new RuntimeException("Git commit failed", e);
        }
    }

    public List<GitVersionResponse> getVersions(String pipelineId) {
        List<PipelineVersion> versions = versionRepository.findByPipelineIdOrderByVersionDesc(pipelineId);
        List<GitVersionResponse> responses = new ArrayList<>();

        for (PipelineVersion version : versions) {
            if (version.getGitCommitSha() != null) {
                GitVersionResponse response = new GitVersionResponse();
                response.setCommitSha(version.getGitCommitSha());
                response.setVersion(version.getVersion());
                response.setTimestamp(version.getCreatedAt());
                if (version.getCreatedBy() != null) {
                    response.setAuthor(version.getCreatedBy().getEmail());
                }
                responses.add(response);
            }
        }

        return responses;
    }

    public GitDiffResponse getDiff(String pipelineId, Integer fromVersion, Integer toVersion) {
        // TODO: Implement diff logic
        GitDiffResponse response = new GitDiffResponse();
        response.setFromVersion(String.valueOf(fromVersion));
        response.setToVersion(String.valueOf(toVersion));
        response.setChanges(new ArrayList<>());
        return response;
    }

    public GitVersionResponse rollback(String pipelineId, Integer version, String userId) {
        PipelineVersion targetVersion = versionRepository
                .findByPipelineIdAndVersion(pipelineId, version)
                .orElseThrow(() -> new RuntimeException("Version not found"));

        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow();

        // Create new version from old version
        pipeline.setSpecJson(targetVersion.getSpecJson());
        pipeline.setVersion(pipeline.getVersion() + 1);
        pipeline = pipelineRepository.save(pipeline);

        // Commit rollback
        return commitPipeline(pipelineId, "Rollback to version " + version, userId);
    }

    private Path getOrCreateRepo(String pipelineId) throws IOException, GitAPIException {
        Path repoPath = Paths.get(gitBasePath, pipelineId);
        
        if (!Files.exists(repoPath)) {
            Files.createDirectories(repoPath);
            Git.init().setDirectory(repoPath.toFile()).call();
        }

        return repoPath;
    }
}

