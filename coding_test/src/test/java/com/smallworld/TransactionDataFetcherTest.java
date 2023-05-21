package com.smallworld;

import com.smallworld.model.Transaction;
import com.smallworld.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TransactionDataFetcherTest {
    @InjectMocks
    private TransactionDataFetcher transactionDataFetcher;
    @Mock
    TransactionService transactionService;

    @Test
    void testGetTotalTransactionAmount_WhenTransactionExist() {
        Mockito.when(transactionService.getAllTransaction()).thenReturn(getTransactions());
        double sumTransactionAmount = transactionDataFetcher.getTotalTransactionAmount();
        Assertions.assertEquals(400.4, sumTransactionAmount);
    }

    @Test
    void testGetTotalTransactionAmount_WhenTransactionDoNotExist() {
        Mockito.when(transactionService.getAllTransaction()).thenReturn(getEmptyTransactions());
        double sumTransactionAmount = transactionDataFetcher.getTotalTransactionAmount();
        Assertions.assertEquals(0.0, sumTransactionAmount);
    }

    @Test
    void testGetTotalTransactionAmountSentBy_WhenTransactionExist() {
        Mockito.when(transactionService.getAllTransaction()).thenReturn(getTransactions());
        double totalTransactionAmountSentBy = transactionDataFetcher.getTotalTransactionAmountSentBy("Tom Shelby");
        Assertions.assertEquals(400.4, totalTransactionAmountSentBy);
    }

    @Test
    void testGetTotalTransactionAmountSentBy_WhenTransactionDoesNotExist() {
        Mockito.when(transactionService.getAllTransaction()).thenReturn(getEmptyTransactions());
        double totalTransactionAmountSentBy = transactionDataFetcher.getTotalTransactionAmountSentBy("Tom Shelby");
        Assertions.assertEquals(0.0, totalTransactionAmountSentBy);
    }

    @Test
    void testGetTotalTransactionAmountSentBy_WhenTransactionExistAndNoSuchSenderExist() {
        Mockito.when(transactionService.getAllTransaction()).thenReturn(getTransactions());
        double totalTransactionAmountSentBy = transactionDataFetcher.getTotalTransactionAmountSentBy("");
        Assertions.assertEquals(0.0, totalTransactionAmountSentBy);
    }

    @Test
    void testGetMaxTransactionAmount_WhenTransactionExist() {
        Mockito.when(transactionService.getAllTransaction()).thenReturn(getTransactions());
        double totalTransactionAmountSentBy = transactionDataFetcher.getMaxTransactionAmount();
        Assertions.assertEquals(150.2, totalTransactionAmountSentBy);
    }

    @Test
    void testGetMaxTransactionAmount_WhenTransactionDoesNotExist() {
        Mockito.when(transactionService.getAllTransaction()).thenReturn(getEmptyTransactions());
        double totalTransactionAmountSentBy = transactionDataFetcher.getMaxTransactionAmount();
        Assertions.assertEquals(0.0, totalTransactionAmountSentBy);
    }

    @Test
    void testCountUniqueClients_WhenTransactionExist() {
        Mockito.when(transactionService.getAllTransaction()).thenReturn(getTransactions());
        double totalTransactionAmountSentBy = transactionDataFetcher.countUniqueClients();
        Assertions.assertEquals(2, totalTransactionAmountSentBy);
    }

    @Test
    void testCountUniqueClients_WhenTransactionDoesNotExist() {
        Mockito.when(transactionService.getAllTransaction()).thenReturn(getEmptyTransactions());
        double totalTransactionAmountSentBy = transactionDataFetcher.countUniqueClients();
        Assertions.assertEquals(0, totalTransactionAmountSentBy);
    }


    private List<Transaction> getTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setMtn(663458);
        transaction1.setAmount(100.0);
        transaction1.setSenderFullName("Tom Shelby");
        transaction1.setSenderAge(22);
        transaction1.setBeneficiaryFullName("Alfie Solomons");
        transaction1.setBeneficiaryAge(33);
        transaction1.setIssueId(1);
        transaction1.setIssueSolved(false);
        transaction1.setIssueMessage("Looks like money laundering");

        Transaction transaction2 = new Transaction();
        transaction2.setMtn(1284564);
        transaction2.setAmount(150.2);
        transaction2.setSenderFullName("Tom Shelby");
        transaction2.setSenderAge(22);
        transaction2.setBeneficiaryFullName("Arthur Shelby");
        transaction2.setBeneficiaryAge(60);
        transaction2.setIssueId(2);
        transaction2.setIssueSolved(true);
        transaction2.setIssueMessage("Never gonna give you up");

        Transaction transaction3 = new Transaction();
        transaction3.setMtn(1284564);
        transaction3.setAmount(150.2);
        transaction3.setSenderFullName("Tom Shelby");
        transaction3.setSenderAge(22);
        transaction3.setBeneficiaryFullName("Arthur Shelby");
        transaction3.setBeneficiaryAge(60);
        transaction3.setIssueId(3);
        transaction3.setIssueSolved(false);
        transaction3.setIssueMessage("Looks like money laundering");

        Transaction transaction4 = new Transaction();
        transaction4.setMtn(1284564);
        transaction4.setAmount(0.0);
        transaction4.setSenderFullName("xyz");
        transaction4.setSenderAge(22);
        transaction4.setBeneficiaryFullName("abc");
        transaction4.setBeneficiaryAge(60);
        transaction4.setIssueId(null);
        transaction4.setIssueSolved(true);
        transaction4.setIssueMessage("test transaction");

        return List.of(transaction1, transaction2, transaction3, transaction4);

    }

    private List<Transaction> getEmptyTransactions() {
        return null;
    }

}