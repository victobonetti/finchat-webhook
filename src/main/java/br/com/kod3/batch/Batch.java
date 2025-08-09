package br.com.kod3.batch;

import br.com.kod3.models.evolution.list.EvolutionListFactory;
import br.com.kod3.models.recurrence.Recurrence;
import br.com.kod3.repositories.recurrence.RecurrenceRepository;
import br.com.kod3.services.evolution.EvolutionApiService;
import br.com.kod3.services.evolution.EvolutionMessageSender;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

import static br.com.kod3.services.util.Messages.conferencia_registro_recorrente;

@ApplicationScoped
public class Batch {

    private final RecurrenceRepository repository;
    private final EvolutionApiService evolutionApiService;

    @Inject
    public Batch(RecurrenceRepository repository, EvolutionApiService evolutionApiService) {
        this.repository = repository;
        this.evolutionApiService = evolutionApiService;
    }

    @Scheduled(cron = "0 0 11 * * ?") // Executa td dia às 11h da manhã
    @Transactional
    public void generateRecorrentTransactions(){
        List<Recurrence> recurrencesFromToday = repository.findByDiaDoMes();

        recurrencesFromToday.forEach(r -> {
            var evo = new EvolutionMessageSender(evolutionApiService, r.getUser().getTelefone());
            evo.send(conferencia_registro_recorrente);
            evo.opts(EvolutionListFactory.getTransactionList(r));
        });
    }
}
