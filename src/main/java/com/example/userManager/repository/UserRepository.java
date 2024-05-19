package com.example.userManager.repository;

import com.example.userManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByMobNum(String mobNum);
}
