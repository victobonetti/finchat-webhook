package br.com.kod3.services.recorrencia;

import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.models.recorrencia.RecorrenciaConverter;
import br.com.kod3.models.transaction.TransactionConverter;
import br.com.kod3.models.transaction.TransactionType;
import br.com.kod3.models.user.User;
import br.com.kod3.repositories.recorrencia.RecorrenciaRepository;
import br.com.kod3.repositories.transaction.TransactionRepository;
import br.com.kod3.services.evolution.EvolutionMessageSender;
import br.com.kod3.services.util.CodigosDeResposta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;

import static br.com.kod3.services.util.CodigosDeResposta.*;
import static br.com.kod3.services.util.Messages.*;
import static br.com.kod3.services.util.Messages.erro_validacao_resposta_transacao;

@ApplicationScoped
public class RecorrenciaService {
    @Inject
    RecorrenciaRepository repository;

    @Inject
    TransactionRepository transactionRepository;

    private void createOne(Recorrencia entity, Boolean createTransaction) {
        repository.persistAndFlush(entity);
        if (createTransaction) {
            var transaction = TransactionConverter.fromRecorrencia(entity);
            transactionRepository.persist(transaction);
        }
    }

    public List<Recorrencia> getRecorrenciasAtivas(String uid) {
        return repository.find("user.id = ?1 and situacao = 'ATIVO'", uid).list();
    }

    public List<Recorrencia> getAllRecorrencias(String uid) {
        return repository.find("user.id = ?1", uid).list();
    }

    public Recorrencia getById(String recorrenciaId) {
        return repository.findById(recorrenciaId);
    }

    @Transactional
    public CodigosDeResposta handle(ConvertedDto converted, User user, EvolutionMessageSender evo) {
        Objects.requireNonNull(converted.getTransactionPayloadDto());

        if (converted.getData().toLowerCase().contains(cancela_transacao)) {
            evo.send(registro_cancelado);
            return CANCELA_TRANSACAO;
        }

        if (converted.getData().toLowerCase().contains(confirma_transacao)) {
            createOne(RecorrenciaConverter.toEntity(converted.getTransactionPayloadDto(), user), true);

            if (converted.getTransactionPayloadDto().getType().equals(TransactionType.RECORRENT_INCOME)) {
                evo.send(receita_recorrente_criada);
                return CONFIRMA_TRANSACAO;
            }

            if (converted.getTransactionPayloadDto().getType().equals(TransactionType.RECORRENT_EXPENSE)) {
                evo.send(gasto_recorrente_criado);
                return CONFIRMA_TRANSACAO;
            }
        }

        evo.send(erro_validacao_resposta_transacao);
        return ERRO_INTERNO;
    }
}
