package br.com.kod3.repositories;

import br.com.kod3.models.recorrencia.PeriodEnum;
import br.com.kod3.models.recorrencia.Recorrencia;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RecorrenciaRepository implements PanacheRepositoryBase<Recorrencia, String> {

    public List<Recorrencia> findByDiaDoMes() {
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();

        String query = "(dayOfMonth = :dayOfMonth AND period = :mensal) OR (dayOfWeek = :dayOfWeek AND period = :semanal)";

        Parameters params = Parameters
                .with("dayOfMonth", dayOfMonth)
                .and("dayOfWeek", dayOfWeek)
                .and("mensal", PeriodEnum.MENSAL)
                .and("semanal", PeriodEnum.SEMANAL);

        return list(query, params);
    }
}


