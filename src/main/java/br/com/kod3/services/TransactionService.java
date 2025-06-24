package br.com.kod3.services;

import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.repositories.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TransactionService {
    @Inject
    TransactionRepository transactionRepository;

    public List<Transaction> findMany(){
        return transactionRepository.findAll().stream().toList();
    }
}
