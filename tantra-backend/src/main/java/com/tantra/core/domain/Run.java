package com.tantra.core.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "runs")
@Data
public class Run {
    @Id
    @Column(name = "run_id")
    private String runId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_id", nullable = false)
    private Pipeline pipeline;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RunStatus status = RunStatus.PENDING;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triggered_by")
    private User triggeredBy;

    @Column(columnDefinition = "JSON")
    private String paramsJson;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum RunStatus {
        PENDING, RUNNING, SUCCESS, FAILED, CANCELLED
    }
}

