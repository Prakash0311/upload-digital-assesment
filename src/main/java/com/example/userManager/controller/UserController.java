package com.example.userManager.controller;

import com.example.userManager.model.User;
import com.example.userManager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create_user")
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/get_users")
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String mobNum,
            @RequestParam(required = false) UUID managerId) {
        List<User> users = userService.getUsers(userId, mobNum, managerId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/delete_user")
    public ResponseEntity<String> deleteUser(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String mobNum) {
        userService.deleteUser(userId, mobNum);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/update_user")
    public ResponseEntity<String> updateUser(
            @RequestParam UUID userId,
            @Valid @RequestBody User updatedUser) {
        userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok("User updated successfully");
    }
}
