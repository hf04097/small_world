package com.smallworld;

import com.smallworld.exception.ServiceException;
import com.smallworld.model.Transaction;
import com.smallworld.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class TransactionDataFetcher {
    @Autowired
    private TransactionService transactionService;

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        List<Transaction> transactions = transactionService.getAllTransaction();
        return transactions.stream().filter(transaction -> transaction.getSenderFullName().equals(senderFullName)).mapToDouble(Transaction::getAmount).sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        Optional<Transaction> optionalTransaction = transactions.stream().max(Comparator.comparingDouble(Transaction::getAmount));
        if(optionalTransaction.isPresent()){
            return optionalTransaction.get().getAmount();
        }
        else{
            throw new ServiceException("Transaction Object Not Found");
        }
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Object> getTransactionsByBeneficiaryName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the 3 transactions with highest amount sorted by amount descending
     */
    public List<Object> getTop3TransactionsByAmount() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the sender with the most total sent amount
     */
    public Optional<Object> getTopSender() {
        throw new UnsupportedOperationException();
    }

}
