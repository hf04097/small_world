package com.smallworld;

import com.smallworld.exception.ServiceException;
import com.smallworld.model.Transaction;
import com.smallworld.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionDataFetcher {
    @Autowired
    private TransactionService transactionService;

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        if (CollectionUtils.isEmpty(transactions)) {
            return 0.0;
        }
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        List<Transaction> transactions = transactionService.getAllTransaction();
        if (CollectionUtils.isEmpty(transactions)) {
            return 0.0;
        }
        return transactions.stream().filter(transaction -> transaction.getSenderFullName().equals(senderFullName)).mapToDouble(Transaction::getAmount).sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        if (CollectionUtils.isEmpty(transactions)) {
            return 0.0;
        }
        Optional<Transaction> optionalTransaction = transactions.stream().max(Comparator.comparingDouble(Transaction::getAmount));
        if (optionalTransaction.isPresent()) {
            return optionalTransaction.get().getAmount();
        } else {
            throw new ServiceException("Transaction Object Not Found");
        }
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        //todo: is this correct?
        List<Transaction> transactions = transactionService.getAllTransaction();
        if (CollectionUtils.isEmpty(transactions)) {
            return 0;
        }
        return transactions.stream().map(Transaction::getSenderFullName).distinct().count();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        List<Transaction> transactions = transactionService.getAllTransaction();
        return transactions.stream().anyMatch(transaction -> transaction.getSenderFullName().equals(clientFullName)
                && Objects.nonNull(transaction.getIssueId()) && transaction.getIssueSolved().equals(false));

    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Object> getTransactionsByBeneficiaryName() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        Map<String, List<Transaction>> beneficiaryMap = transactions.stream().collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
        return new HashMap<>(beneficiaryMap);
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        return transactions.stream().filter(transaction -> transaction.getIssueSolved().equals(false)).map(Transaction::getIssueId).collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {

        List<Transaction> transactions = transactionService.getAllTransaction();
        return transactions.stream().filter(transaction -> transaction.getIssueSolved().equals(true)).map(Transaction::getIssueMessage).toList();
    }

    /**
     * Returns the 3 transactions with highest amount sorted by amount descending
     */
    public List<Object> getTop3TransactionsByAmount() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        return transactions.stream().sorted(Comparator.comparing(Transaction::getAmount).reversed()).limit(3).collect(Collectors.toList());
    }

    /**
     * Returns the sender with the most total sent amount
     */
    public Optional<Object> getTopSender() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        Optional<Transaction> optionalTransaction = transactions.stream().max(Comparator.comparingDouble(Transaction::getAmount));
        if (optionalTransaction.isPresent()) {
            return Optional.ofNullable(optionalTransaction.get().getSenderFullName());
        } else {
            throw new ServiceException("Transaction object not found");
        }
    }

}
