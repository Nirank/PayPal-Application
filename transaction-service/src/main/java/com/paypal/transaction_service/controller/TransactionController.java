package com.paypal.transaction_service.controller;


import com.paypal.transaction_service.entity.Transaction;
import com.paypal.transaction_service.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions/")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }
    @PostMapping("create")
    public ResponseEntity<?> create(@Valid @RequestBody Transaction request) {
        Transaction transaction = service.createTransaction(request);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("all")
    public List<Transaction> getAll() {
        return service.getAllTransactions();
    }
}
