package br.com.pinalli.financeirohome.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContaRequest(
        @NotBlank String descricao,
        @Positive BigDecimal valor,
        @NotNull @FutureOrPresent LocalDate dataVencimento,
        @NotNull Long categoriaId
) {}