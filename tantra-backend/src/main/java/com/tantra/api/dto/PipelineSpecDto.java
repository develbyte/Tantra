package com.tantra.api.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class PipelineSpecDto {
    private String id;
    private String name;
    private String version;
    private Map<String, ParamDefinition> params;
    private List<NodeDto> nodes;
    private List<EdgeDto> edges;
    private ScheduleDto schedule;
    private List<SecretReferenceDto> secrets;

    @Data
    public static class ParamDefinition {
        private String type;
        private String defaultValue;
    }

    @Data
    public static class NodeDto {
        private String id;
        private String type;
        private String name;
        private String image;
        private List<String> command;
        private List<String> inputs;
        private List<String> outputs;
        private ResourceDto resources;
        private Integer retries;
        private Integer timeoutSeconds;
    }

    @Data
    public static class ResourceDto {
        private String cpu;
        private String memory;
    }

    @Data
    public static class EdgeDto {
        private String from;
        private String to;
    }

    @Data
    public static class ScheduleDto {
        private String cron;
    }

    @Data
    public static class SecretReferenceDto {
        private String ref;
        private String target;
    }
}

