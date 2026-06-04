package com.finflow.controller;


import com.finflow.dto.trasactions.UserFinancialSummaryDTO;
import com.finflow.dto.users.UserRequestDTO;
import com.finflow.dto.users.UserResponseDTO;
import com.finflow.services.TransactionService;
import com.finflow.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;

    @PostMapping
    public UserResponseDTO createUser(
            @RequestBody UserRequestDTO request) {

        return userService.createUser(request);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    //read by id
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO request) {

        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{userId}/financial-summary")
    public UserFinancialSummaryDTO getFinancialSummary(
            @PathVariable Long userId) {

        return transactionService.getFinancialSummary(userId);
    }

}
