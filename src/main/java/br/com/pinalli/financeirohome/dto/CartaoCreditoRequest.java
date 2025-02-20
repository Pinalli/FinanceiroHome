package br.com.pinalli.financeirohome.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CartaoCreditoRequest(
        @NotBlank String bandeiraCartao, // Ou "bandeiraCartao", dependendo do backend
        @Positive BigDecimal limite,
        @PositiveOrZero BigDecimal valor,
        @PositiveOrZero BigDecimal limiteDisponivel,
        @PositiveOrZero BigDecimal totalComprasAbertas
) {}