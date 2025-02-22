package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.StatusConta;
import br.com.pinalli.financeirohome.model.TipoConta;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContaResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        TipoConta tipo,
        LocalDate dataVencimento,
        StatusConta status,
        String categoriaNome
) {}