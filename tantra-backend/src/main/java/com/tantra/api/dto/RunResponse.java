package com.tantra.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RunResponse {
    private String runId;
    private String pipelineId;
    private Integer version;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String triggeredBy;
}

