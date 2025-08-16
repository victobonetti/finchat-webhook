package br.com.kod3.models.transaction;

import br.com.kod3.models.util.enums.Category;
import br.com.kod3.models.util.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(
    String id,
    String description,
    BigDecimal value,
    Category category,
    TransactionType type,
    String currency,
    Boolean blocked,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String reccurrenceId,
    String debtId) {}
