package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.ParcelaCompra;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaCompraDTO (
        Long id,
        BigDecimal valorParcela,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        ParcelaCompra status
){}