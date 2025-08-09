package br.com.kod3.repositories.transaction;

import br.com.kod3.models.transaction.Transaction;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class TransactionRepository implements PanacheRepositoryBase<Transaction, String> {

    public Integer getUserStreak(String userId) {
        String sql = "WITH " +
                "  dias_unicos AS ( " +
                "    SELECT DISTINCT CAST(\"createdAt\" AS DATE) AS dia " +
                "    FROM \"Transaction\" " +
                "    WHERE \"userId\" = ?1 " +
                "  ), " +
                "  grupos_de_dias AS ( " +
                "    SELECT " +
                "      dia, " +
                "      (dia - CAST(ROW_NUMBER() OVER (ORDER BY dia) AS INT)) AS grupo " +
                "    FROM dias_unicos " +
                "  ), " +
                "  sequencias_contadas AS ( " +
                "    SELECT " +
                "      COUNT(*) AS tamanho_sequencia, " +
                "      MAX(dia) AS ultimo_dia_da_sequencia " +
                "    FROM grupos_de_dias " +
                "    GROUP BY grupo " +
                "  ) " +
                "SELECT tamanho_sequencia " +
                "FROM sequencias_contadas " +
                "ORDER BY ultimo_dia_da_sequencia DESC " +
                "LIMIT 1";

        var query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, userId);

        try {
            Object result = query.getSingleResult();
            if (result instanceof BigInteger) {
                return ((BigInteger) result).intValue();
            }
            return ((Number) result).intValue();
        } catch (NoResultException e) {
            return 0;
        }
    }

    public List<Transaction> findByUserAndDateRange(String userId, LocalDate start, LocalDate end) {
        return find(
                "user.id = ?1 and createdAt >= ?2 and createdAt <= ?3", // TODO
                Sort.by("createdAt").descending(),
                userId,
                start.atStartOfDay(),
                end.atTime(LocalTime.MAX)
        ).list();
    }

    public BigDecimal getPaidValueFromDebt(String debtId, String uid) {
        return find("\"debtId\" = ?1 and \"user.id\"= ?2 ", debtId, uid)
                .stream()
                .map(Transaction::getValue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
