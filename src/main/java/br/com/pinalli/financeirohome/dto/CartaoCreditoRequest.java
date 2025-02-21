package br.com.pinalli.financeirohome.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CartaoCreditoRequest(
        @NotBlank String bandeiraCartao,
        @NotBlank String numero,
        @Positive BigDecimal limiteTotal, // Alterado de "limite" para "limiteTotal"
        @PositiveOrZero BigDecimal limiteDisponivel,
        @PositiveOrZero BigDecimal totalComprasAbertas,
        @Min(1) @Max(31) Integer diaFechamento,
        @Min(1) @Max(31) Integer diaVencimento
) {}