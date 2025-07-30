package br.com.kod3.services;

import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.transaction.TransactionConverter;
import br.com.kod3.repositories.TransactionRepository;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
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
  @CacheInvalidate(cacheName = "has-transaction-today")
  public void createOne(Transaction transaction, @CacheKey String userId) {
    transactionRepository.persistAndFlush(transaction);
    Log.info("Criada nova transação para o usuario" + userId + ": " + transaction);
  }

  public void createOneFromRecorrencia(Recorrencia recorrencia) {
    var transaction = TransactionConverter.fromRecorrencia(recorrencia);
    transactionRepository.persistAndFlush(transaction);
  }
}
