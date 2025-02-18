package br.com.pinalli.financeirohome.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CompraCartaoRequest(
        @NotBlank String descricao,
        @Positive BigDecimal valorTotal,
        @Min(1) Integer quantidadeParcelas,
        @NotNull Long cartaoId,
        @NotNull Long categoriaId
) {}