package br.com.kod3.models.transaction;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record TransactionsParams(
        @NotNull Boolean findExpenses,
        @NotNull Boolean findIncomes,
        @NotNull Boolean showBlocked,
        @NotNull Boolean showNonBlocked,
        @NotNull @Min(0) @Max(100_000_000) Integer pageIndex,
        @NotNull @Min(0) @Max(100) Integer pageSize,
        @Nullable @PastOrPresent LocalDate dataIni,
        @Nullable @PastOrPresent LocalDate dataFim
        )
{}
