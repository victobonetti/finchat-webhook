package br.com.kod3.repositories;

import br.com.kod3.models.recorrencia.Recorrencia;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RecorrenciaRepository implements PanacheRepositoryBase<Recorrencia, String> {}
