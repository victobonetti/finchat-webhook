package br.com.kod3.services.streak;

import br.com.kod3.models.user.User;
import br.com.kod3.repositories.transaction.TransactionRepository;
import br.com.kod3.services.evolution.EvolutionMessageSender;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

@ApplicationScoped
public class StreakService {
  private final TransactionRepository transactionRepository;

  private HashMap<String, Integer> userStreakMap = new HashMap<>();

  @Inject
  public StreakService(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  public Integer getStreakFromUserId(String userId) {
    Objects.requireNonNull(userId);

    Integer streak;

    if (userStreakMap.containsKey(userId)) {
      streak = userStreakMap.get(userId);
    } else {
      streak = transactionRepository.getUserStreak(userId);
      userStreakMap.put(userId, streak);
    }

    return streak;
  }

  @CacheResult(cacheName = "has-transaction-today")
  public boolean hasTransactionToday(String userId) {
    LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
    LocalDateTime startOfNextDay = LocalDate.now().plusDays(1).atStartOfDay();

    long count =
        transactionRepository.count(
            "user.id = ?1 and createdAt >= ?2 and createdAt < ?3",
            userId,
            startOfDay,
            startOfNextDay);

    return count > 0;
  }

  @Scheduled(cron = "59 59 23 * * ?")
  @CacheInvalidateAll(cacheName = "has-transaction-today")
  void cleanCache() {
    this.userStreakMap = new HashMap<>();
  }

  public void handleStreak(User user, EvolutionMessageSender evo) {
    var streak = getStreakFromUserId(user.getId());
    if (streak == 1) {
      evo.send("Sua ofensiva começou! Continue assim! \uFE0F\u200D\uD83D\uDD25\uD83D\uDC2F");
    } else {
      evo.send(
          "Sua ofensiva está em "
              + streak
              + " dias. Continue assim! \uFE0F\u200D\uD83D\uDD25\uD83D\uDC2F");
    }
  }
}
