package com.tantra.api.dto;

import lombok.Data;

@Data
public class GitCommitRequest {
    private String pipelineId;
    private String message;
}

