package com.polyglot.task.controller;

import com.polyglot.task.model.Task;
import com.polyglot.task.repository.TaskRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/java/v1/tasks")
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return repository.save(task);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "tasks", key = "#id")
    public Task getTask(@PathVariable UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "tasks", key = "#id")
    public void deleteTask(@PathVariable UUID id) {
        repository.deleteById(id);
    }
}