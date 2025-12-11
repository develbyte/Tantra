package com.tantra.api.dto;

import lombok.Data;
import java.util.Map;

@Data
public class RunRequest {
    private Map<String, String> params;
    private String environment;
}

