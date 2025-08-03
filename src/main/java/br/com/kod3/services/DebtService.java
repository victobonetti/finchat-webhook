package br.com.kod3.services;

import br.com.kod3.models.divida.Debt;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.transaction.TransactionPayloadDto;
import br.com.kod3.repositories.DebtRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class DebtService {
    @Inject
    DebtRepository repository;

    @Transactional
    public void createOne(Debt entity) {
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
}
