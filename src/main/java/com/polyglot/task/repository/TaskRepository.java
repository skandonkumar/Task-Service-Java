package com.polyglot.task.repository;

import com.polyglot.task.model.Task;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TaskRepository extends CassandraRepository<Task, UUID> {
}