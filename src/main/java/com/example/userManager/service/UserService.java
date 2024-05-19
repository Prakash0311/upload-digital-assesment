package com.example.userManager.service;

import com.example.userManager.model.User;
import com.example.userManager.model.Manager;
import com.example.userManager.repository.UserRepository;
import com.example.userManager.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    public User createUser(User user) {
        user.setFullName(user.getFullName().trim());
        user.setMobNum(formatMobileNumber(user.getMobNum()));
        user.setPanNum(user.getPanNum().toUpperCase());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsActive(true);

        if (user.getManager() != null) {
            UUID managerId = user.getManager().getManagerId();
            Optional<Manager> managerOptional = managerRepository.findById(managerId);

            if (managerOptional.isEmpty() || !managerOptional.get().getIsActive()) {
                throw new IllegalArgumentException("Invalid or inactive manager ID");
            }
            user.setManager(managerOptional.get());
        }

        // Save user to repository
        return userRepository.save(user);
    }

    public List<User> getUsers(UUID userId, String mobNum, UUID managerId) {
        if (userId != null) {
            return userRepository.findAllById(List.of(userId));
        } else if (mobNum != null) {
            User user = userRepository.findByMobNum(mobNum);
            return user != null ? List.of(user) : List.of();
        } else if (managerId != null) {
            return userRepository.findAll().stream()
                    .filter(user -> user.getManager() != null && user.getManager().getManagerId().equals(managerId))
                    .collect(Collectors.toList());
        }
        return userRepository.findAll();
    }

    public void deleteUser(UUID userId, String mobNum) {
        if (userId != null) {
            userRepository.deleteById(userId);
        } else if (mobNum != null) {
            User user = userRepository.findByMobNum(mobNum);
            if (user != null) {
                userRepository.delete(user);
            } else {
                throw new IllegalArgumentException("User not found");
            }
        }
    }

    public void updateUser(UUID userId, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        if (updatedUser.getFullName() != null) {
            user.setFullName(updatedUser.getFullName().trim());
        }
        if (updatedUser.getMobNum() != null) {
            user.setMobNum(formatMobileNumber(updatedUser.getMobNum()));
        }
        if (updatedUser.getPanNum() != null) {
            user.setPanNum(updatedUser.getPanNum().toUpperCase());
        }
        if (updatedUser.getManager() != null) {
            Optional<Manager> manager = managerRepository.findById(updatedUser.getManager().getManagerId());
            if (!manager.isPresent() || !manager.get().getIsActive()) {
                throw new IllegalArgumentException("Invalid or inactive manager ID");
            }
            user.setManager(updatedUser.getManager());
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private String formatMobileNumber(String mobNum) {
        if (mobNum.startsWith("+91")) {
            return mobNum.substring(3);
        } else if (mobNum.startsWith("0")) {
            return mobNum.substring(1);
        }
        return mobNum;
    }
}

