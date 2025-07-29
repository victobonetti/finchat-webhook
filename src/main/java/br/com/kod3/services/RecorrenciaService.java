package br.com.kod3.services;

import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.repositories.RecorrenciaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RecorrenciaService {
    @Inject
    RecorrenciaRepository repository;

    public void createOne(Recorrencia entity) {
        repository.persist(entity);
    }
}
