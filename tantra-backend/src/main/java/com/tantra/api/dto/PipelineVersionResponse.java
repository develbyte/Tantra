package com.tantra.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PipelineVersionResponse {
    private String id;
    private Integer version;
    private String gitCommitSha;
    private String createdBy;
    private LocalDateTime createdAt;
}

