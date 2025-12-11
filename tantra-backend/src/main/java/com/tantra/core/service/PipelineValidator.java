package com.tantra.core.service;

import com.tantra.api.dto.PipelineSpecDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PipelineValidator {

    public ValidationResult validate(PipelineSpecDto spec) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // Validate nodes
        if (spec.getNodes() == null || spec.getNodes().isEmpty()) {
            errors.add("Pipeline must have at least one node");
        } else {
            Set<String> nodeIds = new HashSet<>();
            for (PipelineSpecDto.NodeDto node : spec.getNodes()) {
                if (node.getId() == null || node.getId().isEmpty()) {
                    errors.add("Node must have an id");
                } else if (nodeIds.contains(node.getId())) {
                    errors.add("Duplicate node id: " + node.getId());
                } else {
                    nodeIds.add(node.getId());
                }

                if (node.getImage() == null || node.getImage().isEmpty()) {
                    errors.add("Node " + node.getId() + " must have an image");
                }
            }

            // Validate edges
            if (spec.getEdges() != null) {
                for (PipelineSpecDto.EdgeDto edge : spec.getEdges()) {
                    if (!nodeIds.contains(edge.getFrom())) {
                        errors.add("Edge references unknown node: " + edge.getFrom());
                    }
                    if (!nodeIds.contains(edge.getTo())) {
                        errors.add("Edge references unknown node: " + edge.getTo());
                    }
                }

                // Check for cycles
                if (hasCycle(spec, nodeIds)) {
                    errors.add("Pipeline contains a cycle");
                }
            }
        }

        return new ValidationResult(errors, warnings);
    }

    private boolean hasCycle(PipelineSpecDto spec, Set<String> nodeIds) {
        Map<String, List<String>> graph = new HashMap<>();
        for (String nodeId : nodeIds) {
            graph.put(nodeId, new ArrayList<>());
        }

        if (spec.getEdges() != null) {
            for (PipelineSpecDto.EdgeDto edge : spec.getEdges()) {
                graph.get(edge.getFrom()).add(edge.getTo());
            }
        }

        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        for (String nodeId : nodeIds) {
            if (hasCycleDFS(nodeId, graph, visited, recStack)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycleDFS(String node, Map<String, List<String>> graph,
                                Set<String> visited, Set<String> recStack) {
        if (recStack.contains(node)) {
            return true;
        }
        if (visited.contains(node)) {
            return false;
        }

        visited.add(node);
        recStack.add(node);

        for (String neighbor : graph.get(node)) {
            if (hasCycleDFS(neighbor, graph, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(node);
        return false;
    }

    public static class ValidationResult {
        private final List<String> errors;
        private final List<String> warnings;
        private final boolean valid;

        public ValidationResult(List<String> errors, List<String> warnings) {
            this.errors = errors;
            this.warnings = warnings;
            this.valid = errors.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }

        public List<String> getWarnings() {
            return warnings;
        }

        public boolean isValid() {
            return valid;
        }
    }
}

