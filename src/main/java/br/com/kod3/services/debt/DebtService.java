package br.com.kod3.services.debt;

import br.com.kod3.models.divida.Debt;
import br.com.kod3.models.divida.DebtConverter;
import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.transaction.TransactionPayloadDto;
import br.com.kod3.models.user.User;
import br.com.kod3.repositories.debt.DebtRepository;
import br.com.kod3.services.evolution.EvolutionMessageSender;
import br.com.kod3.services.util.CodigosDeResposta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static br.com.kod3.services.util.CodigosDeResposta.*;
import static br.com.kod3.services.util.Messages.*;

@ApplicationScoped
public class DebtService {
    @Inject
    DebtRepository repository;

    @Transactional
    private void createOne(Debt entity) {
        repository.persist(entity);
    }

    public List<Debt> getDebitsFromUser(String uid) {
        return repository.find("user.id = ?1", uid).list();
    }

    public Debt getDebtById(String debtId) {
        return repository.findById(debtId);
    }

    public void updateDebit(Debt debt, TransactionPayloadDto transactionPayloadDto) {
        var val = debt.getPaidValue().add(transactionPayloadDto.getValue()).longValue();
        repository.update("set paidValue = ?1 where id = ?2", BigDecimal.valueOf(val), debt.getId());
    }

    public CodigosDeResposta handle(ConvertedDto converted, User user, EvolutionMessageSender evo) {
        Objects.requireNonNull(converted.getTransactionPayloadDto());

        if (converted.getData().toLowerCase().contains(cancela_transacao)) {
            evo.send(registro_cancelado);
            return CANCELA_TRANSACAO;
        }

        if (converted.getData().toLowerCase().contains(confirma_transacao)) {
            createOne(DebtConverter.toEntity(converted.getTransactionPayloadDto(), user));

            evo.send(divida_criada);
            return CONFIRMA_TRANSACAO;
        }

        evo.send(erro_validacao_resposta_transacao);
        return ERRO_INTERNO;
    }
}
