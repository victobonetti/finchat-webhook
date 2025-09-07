package br.com.kod3.repositories.recurrence;

import br.com.kod3.models.recurrence.PeriodEnum;
import br.com.kod3.models.recurrence.Recurrence;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RecurrenceRepository implements PanacheRepositoryBase<Recurrence, String> {

  public List<Recurrence> findByDiaDoMes() {
    int dayOfMonth = LocalDate.now().getDayOfMonth();
    int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();

    String query =
        "(dayOfMonth = :dayOfMonth AND period = :mensal) OR (dayOfWeek = :dayOfWeek AND period = :semanal)";

    Parameters params =
        Parameters.with("dayOfMonth", dayOfMonth)
            .and("dayOfWeek", dayOfWeek)
            .and("mensal", PeriodEnum.MENSAL)
            .and("semanal", PeriodEnum.SEMANAL);

    return list(query, params);
  }
}
