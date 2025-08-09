package br.com.kod3.repositories.debt;

import br.com.kod3.models.divida.Debt;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DebtRepository implements PanacheRepositoryBase<Debt, String> {}
