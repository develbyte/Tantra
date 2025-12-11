package com.tantra.api;

import com.tantra.api.dto.PipelineCreateRequest;
import com.tantra.api.dto.PipelineResponse;
import com.tantra.api.dto.ValidationResponse;
import com.tantra.core.service.PipelineService;
import com.tantra.core.service.PipelineValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pipelines")
@RequiredArgsConstructor
public class PipelineController {
    private final PipelineService pipelineService;
    private final PipelineValidator validator;

    @PostMapping
    public ResponseEntity<PipelineResponse> createPipeline(
            @Valid @RequestBody PipelineCreateRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        PipelineResponse response = pipelineService.createPipeline(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PipelineResponse> getPipeline(@PathVariable String id) {
        PipelineResponse response = pipelineService.getPipeline(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PipelineResponse> updatePipeline(
            @PathVariable String id,
            @Valid @RequestBody PipelineCreateRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        PipelineResponse response = pipelineService.updatePipeline(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipeline(@PathVariable String id) {
        pipelineService.deletePipeline(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<ValidationResponse> validatePipeline(@PathVariable String id) {
        PipelineResponse pipeline = pipelineService.getPipeline(id);
        PipelineValidator.ValidationResult result = validator.validate(pipeline.getSpec());

        ValidationResponse response = new ValidationResponse();
        response.setValid(result.isValid());
        response.setErrors(result.getErrors());
        response.setWarnings(result.getWarnings());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<PipelineResponse> getPipelineVersions(@PathVariable String id) {
        PipelineResponse response = pipelineService.getPipeline(id);
        return ResponseEntity.ok(response);
    }
}

