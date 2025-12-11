package com.tantra.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class GitDiffResponse {
    private String fromVersion;
    private String toVersion;
    private List<DiffChange> changes;

    @Data
    public static class DiffChange {
        private String type; // ADDED, REMOVED, MODIFIED
        private String path;
        private String oldValue;
        private String newValue;
    }
}

