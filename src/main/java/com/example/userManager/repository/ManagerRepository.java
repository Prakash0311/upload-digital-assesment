package com.example.userManager.repository;

import com.example.userManager.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    Optional<Manager> findById(UUID managerId);
}

