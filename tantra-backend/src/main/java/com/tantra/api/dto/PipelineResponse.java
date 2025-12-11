package com.tantra.api.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PipelineResponse {
    private String id;
    private String name;
    private Integer version;
    private PipelineSpecDto spec;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PipelineVersionResponse> versions;
}

