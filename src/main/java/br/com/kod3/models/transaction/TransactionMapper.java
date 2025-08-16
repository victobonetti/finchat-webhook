package br.com.kod3.models.transaction;

public class TransactionMapper {
    public static TransactionResponseDto fromEntity(Transaction e) {
        var debtId = e.getDebt() != null ? e.getDebt().getId() : null;
        var reccurenceId = e.getRecurrence() != null ? e.getRecurrence().getId() : null;

        return new TransactionResponseDto(
                e.getId(),
                e.getDescription(),
                e.getValue(),
                e.getCategory(),
                e.getType(),
                e.getCurrency(),
                e.getBlocked(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                reccurenceId,
                debtId
        );
    }
}
