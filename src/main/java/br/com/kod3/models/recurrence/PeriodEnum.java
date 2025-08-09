package br.com.kod3.models.recurrence;

import java.util.Objects;

public enum PeriodEnum {
    MENSAL,
    SEMANAL;

    public static PeriodEnum fromString(String s) {
        if (Objects.equals(s, "MENSAL")) {
            return MENSAL;
        }

        if (Objects.equals(s, "SEMANAL")){
            return SEMANAL;
        }

        return null;
    }
}
