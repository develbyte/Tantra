package com.tantra.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GitVersionResponse {
    private String commitSha;
    private String message;
    private String author;
    private LocalDateTime timestamp;
    private Integer version;
}

