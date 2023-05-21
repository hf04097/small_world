package com.smallworld.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.exception.ServiceException;
import com.smallworld.model.Transaction;
import com.smallworld.service.TransactionService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    @Value("${datasource.json.file-location}")
    private String jsonFileLocation;

    private static List<Transaction> transactions;

    @PostConstruct
        // Loading json file one and storing it in static variable
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
            log.info("loading file");
            File initialFile = new File(jsonFileLocation);
            transactions = mapper.readValue(initialFile, new TypeReference<List<Transaction>>() {
            });
        } catch (Exception e) {
            throw new ServiceException("Transaction Object Not Found");
        }
    }
}
