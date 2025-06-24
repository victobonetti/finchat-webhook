package br.com.kod3.models.evolution.pool;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EvolutionPoolDto {
    private String number;
    private String name;
    private final Integer selectableCount = 1; // Os DTOs da aplicação estão apenas configurados para single select
    private List<String> values;
}
