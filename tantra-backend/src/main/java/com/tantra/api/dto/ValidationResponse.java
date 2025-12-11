package com.tantra.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class ValidationResponse {
    private boolean valid;
    private List<String> errors;
    private List<String> warnings;
}

