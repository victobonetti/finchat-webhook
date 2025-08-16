package br.com.kod3.services.recurrence;

import static br.com.kod3.services.util.CodigoDeResposta.*;
import static br.com.kod3.services.util.Messages.*;
import static br.com.kod3.services.util.Messages.erro_validacao_resposta_transacao;

import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.recurrence.Recurrence;
import br.com.kod3.models.recurrence.RecurrenceConverter;
import br.com.kod3.models.transaction.TransactionConverter;
import br.com.kod3.models.user.User;
import br.com.kod3.models.util.enums.TransactionType;
import br.com.kod3.repositories.recurrence.RecurrenceRepository;
import br.com.kod3.repositories.transaction.TransactionRepository;
import br.com.kod3.services.evolution.EvolutionMessageSender;
import br.com.kod3.services.util.CodigoDeResposta;
import br.com.kod3.services.util.FinchatHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class RecurrenceService implements FinchatHandler {
  private final RecurrenceRepository repository;
  private final TransactionRepository transactionRepository;

  @Inject
  public RecurrenceService(
      RecurrenceRepository repository, TransactionRepository transactionRepository) {
    this.repository = repository;
    this.transactionRepository = transactionRepository;
  }

  private void createOne(Recurrence entity, Boolean createTransaction) {
    repository.persistAndFlush(entity);
    if (createTransaction) {
      var transaction = TransactionConverter.fromRecurrence(entity);
      transactionRepository.persist(transaction);
    }
  }

  public List<Recurrence> getAllRecurrences(String uid) {
    return repository.find("user.id = ?1", uid).list();
  }

  public Recurrence getById(String recurrenceId) {
    return repository.findById(recurrenceId);
  }

  @Transactional
  @Override
  public CodigoDeResposta handle(ConvertedDto converted, User user, EvolutionMessageSender evo) {
    Objects.requireNonNull(converted.getTransactionPayloadDto());

    if (converted.getData().toLowerCase().contains(cancela_transacao)) {
      evo.send(registro_cancelado);
      return CANCELA_TRANSACAO;
    }

    if (converted.getData().toLowerCase().contains(confirma_transacao)) {
      createOne(RecurrenceConverter.toEntity(converted.getTransactionPayloadDto(), user), true);

      if (converted.getTransactionPayloadDto().getType().equals(TransactionType.RECURRING_INCOME)) {
        evo.send(receita_recorrente_criada);
        return CONFIRMA_TRANSACAO;
      }

      if (converted
          .getTransactionPayloadDto()
          .getType()
          .equals(TransactionType.RECURRING_EXPENSE)) {
        evo.send(gasto_recorrente_criado);
        return CONFIRMA_TRANSACAO;
      }
    }

    evo.send(erro_validacao_resposta_transacao);
    return ERRO_INTERNO;
  }
}
