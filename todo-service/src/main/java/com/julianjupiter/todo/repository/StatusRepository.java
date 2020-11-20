package com.julianjupiter.todo.repository;

import com.julianjupiter.todo.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}
