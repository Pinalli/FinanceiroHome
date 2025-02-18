package br.com.pinalli.financeirohome.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CartaoCreditoRequest(
        @NotBlank String nome,
        @NotBlank String numero,
        @Min(1) @Max(31) Integer diaFechamento,
        @Min(1) @Max(31) Integer diaVencimento,
        @Positive BigDecimal limiteTotal
) {}




