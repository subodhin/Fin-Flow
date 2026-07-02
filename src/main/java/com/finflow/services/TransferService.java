package com.finflow.services;
import com.finflow.dto.trasactions.TransferRequestDTO;
import com.finflow.entity.Transaction;
import com.finflow.entity.User;
import com.finflow.enums.TransactionType;
import com.finflow.repository.TransactionRepository;
import com.finflow.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void transferMoney(TransferRequestDTO request) {

        User sender = userRepository.findById(request.getFromUserId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(request.getToUserId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient Balance");
        }

        sender.setBalance(
                sender.getBalance().subtract(request.getAmount()));

        receiver.setBalance(
                receiver.getBalance().add(request.getAmount()));

        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction debitTransaction = Transaction.builder()
                .user(sender)
                .amount(request.getAmount())
                .type(TransactionType.EXPENSE)
                .description("Money Transfer")
                .build();

        Transaction creditTransaction = Transaction.builder()
                .user(receiver)
                .amount(request.getAmount())
                .type(TransactionType.INCOME)
                .description("Money Transfer")
                .build();

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        // Uncomment this to test rollback
        // throw new RuntimeException("Rollback Testing");
    }
}