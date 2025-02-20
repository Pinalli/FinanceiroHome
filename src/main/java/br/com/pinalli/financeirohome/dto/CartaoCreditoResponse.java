package br.com.pinalli.financeirohome.dto;

import java.math.BigDecimal;

public record CartaoCreditoResponse(
        Long id,
        String bandeiraCartao,
        String numero,
        Integer diaFechamento,
        Integer diaVencimento,
        BigDecimal limiteTotal,
        BigDecimal limiteDisponivel
) {}