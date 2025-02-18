package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.StatusParcela;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaResponse(
        Long id,
        BigDecimal valor,
        LocalDate dataVencimento,
        StatusParcela status
) {}