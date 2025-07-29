package br.com.kod3.services;

import br.com.kod3.models.divida.Debt;
import br.com.kod3.repositories.DebtRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DebtService {
    @Inject
    DebtRepository repository;

    public void createOne(Debt entity) {
        repository.persist(entity);
    }
}
