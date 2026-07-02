package com.finflow.controller;

import com.finflow.dto.trasactions.TransferRequestDTO;
import com.finflow.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<String> transferMoney(
            @RequestBody TransferRequestDTO request) {

        transferService.transferMoney(request);

        return ResponseEntity.ok("Transfer Successful");
    }
}