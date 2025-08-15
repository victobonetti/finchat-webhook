package br.com.kod3.models.verificationcode;

import jakarta.validation.constraints.NotBlank;

public record CreateVerificationCodeRequestDto(@NotBlank String telefone) {}
