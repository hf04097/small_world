package com.smallworld;

import com.smallworld.exception.ServiceException;
import com.smallworld.model.Transaction;
import com.smallworld.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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
        List<Transaction> transactions = transactionService.getAllTransaction();
        if (CollectionUtils.isEmpty(transactions)) {
            return 0;
        }
        long senderCount = transactions.stream().map(Transaction::getSenderFullName).distinct().count();
        long receiverCount = transactions.stream().map(Transaction::getSenderFullName).distinct().count();
        return senderCount + receiverCount;
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        List<Transaction> transactions = transactionService.getAllTransaction();
        if (CollectionUtils.isEmpty(transactions)) {
            return false;
        }
        // not checking issue id too as if issue contain no issues then IssueSolved is true
        return transactions.stream().anyMatch(transaction -> (transaction.getSenderFullName().equals(clientFullName) ||
                transaction.getBeneficiaryFullName().equals(clientFullName)) && transaction.getIssueSolved().equals(false));
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Object> getTransactionsByBeneficiaryName() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        if (CollectionUtils.isEmpty(transactions)) {
            return Collections.emptyMap();
        }

        Map<String, List<Transaction>> beneficiaryMap = transactions.stream().collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));

        //changing value to required object type from List<Transaction>
        return new HashMap<>(beneficiaryMap);
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        if (CollectionUtils.isEmpty(transactions)) {
            return new HashSet<>();
        }
        return transactions.stream().filter(transaction -> transaction.getIssueSolved().equals(false)).map(Transaction::getIssueId).collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        if (CollectionUtils.isEmpty(transactions)) {
            return new ArrayList<>();
        }

        // checking issue id too as if issue contain no issues then IssueSolved is true
        return transactions.stream().filter(transaction -> Objects.nonNull(transaction.getIssueId()) &&
                transaction.getIssueSolved().equals(true)).map(Transaction::getIssueMessage).toList();
    }

    /**
     * Returns the 3 transactions with highest amount sorted by amount descending
     */
    public List<Object> getTop3TransactionsByAmount() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        if (CollectionUtils.isEmpty(transactions)) {
            return new ArrayList<>();
        }

        //finding unique transactions as transaction can due to list of issues
        List<Transaction> uniqueTransaction = transactions.stream().filter(distinctByKey(Transaction::getMtn)).toList();
        return uniqueTransaction.stream().sorted(Comparator.comparing(Transaction::getAmount).reversed()).limit(3).collect(Collectors.toList());
    }

    /**
     * Returns the sender with the most total sent amount
     */
    public Optional<Object> getTopSender() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        if (CollectionUtils.isEmpty(transactions)) {
            throw new ServiceException("Transaction object not found");
        }

        //grouping by sender and using sum as aggregate function. Then from that result finding the sender with max sum
        Map<String, Double> senderWithTotalAmountMap = transactions.stream().collect(Collectors.groupingBy(Transaction::getSenderFullName,
                Collectors.summingDouble(Transaction::getAmount)));
        return Optional.ofNullable(Collections.max(senderWithTotalAmountMap.entrySet(), Map.Entry.comparingByValue()).getKey());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
