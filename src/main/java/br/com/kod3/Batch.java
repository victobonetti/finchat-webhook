package br.com.kod3;

import br.com.kod3.models.evolution.list.EvolutionListFactory;
import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.repositories.RecorrenciaRepository;
import br.com.kod3.services.EvolutionApiService;
import br.com.kod3.services.EvolutionMessageSender;
import br.com.kod3.services.TransactionService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

import static br.com.kod3.services.Messages.conferencia_registro_recorrente;

@ApplicationScoped
public class Batch {

    @Inject
    RecorrenciaRepository repository;

    @Inject
    TransactionService transactionService;

    @Inject
    EvolutionApiService evolutionApiService;

    @Scheduled(cron = "0 0 8 * * ?") // Executa todo dia às 8h da manhã
    @Transactional
    public void generateRecorrentTransactions(){
        List<Recorrencia> recorrenciasDoDia = repository.findByDiaDoMes();

        recorrenciasDoDia.forEach(recorrencia -> {
            var evo = new EvolutionMessageSender(evolutionApiService, recorrencia.getUser().getTelefone());
            evo.send(conferencia_registro_recorrente);
            evo.opts(EvolutionListFactory.getTransactionRegistryPoolFromRecorrencia(recorrencia));
        });
    }
}
