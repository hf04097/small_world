package com.smallworld.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.model.Transaction;
import com.smallworld.service.TransactionService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private static List<Transaction> transactions;

    @PostConstruct
    void initTransaction() {
        getAllTransactions();
    }

    @Override
    public List<Transaction> getAllTransaction() {
        return transactions;
    }

    private void getAllTransactions() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File initialFile = new File("src/main/java/resources/transactions.json");
            transactions = mapper.readValue(initialFile, new TypeReference<List<Transaction>>() {
            });
        } catch (Exception e) {
            log.error("Unable to load transaction: " + e.getMessage());
        }
    }
}
