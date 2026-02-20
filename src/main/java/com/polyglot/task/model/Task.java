package com.polyglot.task.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.util.UUID;

@Table("tasks")
public class Task {
    @PrimaryKey
    private UUID id;
    private String title;
    private String description;
    private String status;

    public Task() {
        this.id = UUID.randomUUID();
        this.status = "PENDING";
    }

    // Standard Getters and Setters omitted for brevity
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}