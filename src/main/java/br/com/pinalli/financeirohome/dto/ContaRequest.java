package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContaRequest(
        @NotBlank String descricao,
        @Positive BigDecimal valor,
        @NotNull LocalDate dataVencimento,
        @NotNull TipoConta tipo,
        @NotNull Long categoriaId
) {}