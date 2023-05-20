package com.smallworld;

import com.smallworld.model.Transaction;
import com.smallworld.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TransactionDataFetcherTest {
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void testGetTotalTransactionAmount(){
        List<Transaction> transactions = transactionService.getAllTransaction();

    }


}