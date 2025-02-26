package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.ParcelaCompra;
import br.com.pinalli.financeirohome.model.StatusParcelaCompra;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaResponse(
        Long id,
        BigDecimal valor,
        LocalDate dataVencimento,
        StatusParcelaCompra status,
        Integer numeroParcela) {
    // Metodo estático para converter ParcelaCompra em ParcelaResponse
    public static ParcelaResponse fromParcelaCompra(ParcelaCompra parcelaCompra) {
        return new ParcelaResponse(
                parcelaCompra.getId(),
                parcelaCompra.getValor(),
                parcelaCompra.getDataVencimento(),
                parcelaCompra.getStatus(),
                parcelaCompra.getNumeroParcela());
    }
}