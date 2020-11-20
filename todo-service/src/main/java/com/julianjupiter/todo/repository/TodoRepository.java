package com.julianjupiter.todo.repository;

import com.julianjupiter.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserId(Long id);

    List<Todo> findByUserUsername(String username);
}
