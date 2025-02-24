package br.com.pinalli.financeirohome.dto;


import br.com.pinalli.financeirohome.model.TipoConta;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContaRequest(
        @NotBlank String descricao,
        @Positive BigDecimal valor,
        @NotBlank String tipo, // "PAGAR" ou "RECEBER"
        @NotNull LocalDate dataVencimento,
        @NotBlank String status, // "PENDENTE", "PAGA" ou "RECEBIDA"
        LocalDate dataPagamento, // Opcional
        String observacao, // Opcional
        @NotNull Long usuarioId,
        String usuarioNome,
        @NotNull Long categoriaId,
        String categoriaNome
) {}