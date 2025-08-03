package br.com.kod3.services.formatters;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public interface Formatter {

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US);
    DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US);


    class DailySummary {
        BigDecimal totalSpent = BigDecimal.ZERO;
        BigDecimal totalSaved = BigDecimal.ZERO;
        List<String> spentDetails = new ArrayList<>();
        List<String> savedDetails = new ArrayList<>();

        void addExpense(BigDecimal value, String detail) {
            this.totalSpent = this.totalSpent.add(value);
            this.spentDetails.add(detail);
        }

        void addSaving(BigDecimal value, String detail) {
            this.totalSaved = this.totalSaved.add(value);
            this.savedDetails.add(detail);
        }
    }

    class FormatedStringBuilder {
        private final StringBuilder builder = new StringBuilder();

        public void append(String s){
            builder.append(s).append("\n");
        }

        public String toString() {
            return builder.toString();
        }
    }
}
