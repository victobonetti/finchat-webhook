package br.com.kod3;

import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.repositories.RecorrenciaRepository;
import br.com.kod3.services.TransactionService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class Batch {

    @Inject
    RecorrenciaRepository repository;

    @Inject
    TransactionService transactionService;

    @Scheduled(cron = "0 0 5 * * ?") // Executa todo dia às 5h da manhã
    @Transactional
    public void generateRecorrentTransactions(){
        List<Recorrencia> recorrenciasDoDia = repository.findByDiaDoMes();

        recorrenciasDoDia.forEach(recorrencia -> {
            transactionService.createOneFromRecorrencia(recorrencia);
        });
    }
}
