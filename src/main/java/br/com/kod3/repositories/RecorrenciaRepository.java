package br.com.kod3.repositories;

import br.com.kod3.models.recorrencia.Recorrencia;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RecorrenciaRepository implements PanacheRepositoryBase<Recorrencia, String> {
    public List<Recorrencia> findByDiaDoMes() {
        var currDay = LocalDate.now().getDayOfMonth();
        return getEntityManager()
                .createQuery("SELECT * FROM recorrencia WHERE EXTRACT(DAY FROM paymentDay) = ?1", Recorrencia.class)
                .setParameter(1, currDay)
                .getResultList();
    }
}
