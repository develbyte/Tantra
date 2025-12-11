package com.tantra.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PipelineCreateRequest {
    @NotBlank
    private String name;

    @NotNull
    private PipelineSpecDto spec;
}

