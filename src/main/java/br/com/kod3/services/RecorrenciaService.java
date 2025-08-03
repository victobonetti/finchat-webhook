package br.com.kod3.services;

import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.repositories.RecorrenciaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class RecorrenciaService {
    @Inject
    RecorrenciaRepository repository;

    @Transactional
    public void createOne(Recorrencia entity) {
        repository.persistAndFlush(entity);
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
}
