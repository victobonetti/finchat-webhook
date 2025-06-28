package br.com.kod3.services;

import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.repositories.TransactionRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TransactionService {
  @Inject TransactionRepository transactionRepository;

  public List<Transaction> findMany() {
    return transactionRepository.findAll().stream().toList();
  }

  @Transactional
  public void createOne(Transaction transaction) {
    transactionRepository.persistAndFlush(transaction);
    Log.info("Criada nova transação: " + transaction);
  }
}
