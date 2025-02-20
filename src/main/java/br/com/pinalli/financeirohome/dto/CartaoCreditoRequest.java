package br.com.pinalli.financeirohome.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CartaoCreditoRequest(
        @NotBlank String bandeiraCartao,
        @NotBlank @Size(min = 13, max = 19) String numero,
        @Positive BigDecimal limite,
        @PositiveOrZero BigDecimal limiteDisponivel,
        @PositiveOrZero BigDecimal totalComprasAbertas,
        @Min(1) @Max(31) Integer diaFechamento,
        @Min(1) @Max(31) Integer diaVencimento
) {}
