package br.com.kod3.models.evolution;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConvertedDto {
    @NotBlank
    private MessageType type;
    @NotBlank
    private String telefone;
    @Nullable
    private String base64;
    @Nullable
    private String textMessage;
}
