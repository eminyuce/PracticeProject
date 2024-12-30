package com.acqu.co.excel.converter.actuator.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_name", nullable = false)
    private String applicationName;

    @Column(name = "log_level", nullable = false)
    private String logLevel;

    @Column(name = "log_message", nullable = false, columnDefinition = "TEXT")
    private String logMessage;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    // Default constructor (required by JPA)
    public Log() {}

    // Parameterized constructor for convenience
    public Log(String applicationName, String logLevel, String logMessage, LocalDateTime timestamp) {
        this.applicationName = applicationName;
        this.logLevel = logLevel;
        this.logMessage = logMessage;
        this.timestamp = timestamp;
    }
}