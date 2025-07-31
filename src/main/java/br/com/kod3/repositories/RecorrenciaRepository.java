package br.com.kod3.repositories;

import br.com.kod3.models.recorrencia.PeriodEnum;
import br.com.kod3.models.recorrencia.Recorrencia;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RecorrenciaRepository implements PanacheRepositoryBase<Recorrencia, String> {

    public List<Recorrencia> findByDiaDoMes() {
        int dayOfMonth = LocalDate.now().getDayOfMonth();         // 1–31
        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue(); // 1 (Monday) – 7 (Sunday)

        List<Recorrencia> mensal = getEntityManager()
                .createQuery("""
                        SELECT r FROM Recorrencia r
                        WHERE EXTRACT(DAY FROM r.paymentDay) = :dayOfMonth
                        AND r.period = :period
                        """, Recorrencia.class)
                .setParameter("dayOfMonth", dayOfMonth)
                .setParameter("period", PeriodEnum.MENSAL)
                .getResultList();

        List<Recorrencia> semanal = getEntityManager()
                .createQuery("""
                        SELECT r FROM Recorrencia r
                        WHERE EXTRACT(DOW FROM r.paymentDay) = :dayOfWeek
                        AND r.period = :period
                        """, Recorrencia.class)
                .setParameter("dayOfWeek", dayOfWeek % 7) // convert Java's 1–7 (Mon–Sun) to PostgreSQL's 0–6 (Sun–Sat)
                .setParameter("period", PeriodEnum.SEMANAL)
                .getResultList();

        // Combine both lists
        List<Recorrencia> result = new ArrayList<>();
        result.addAll(mensal);
        result.addAll(semanal);

        return result;
    }
}


