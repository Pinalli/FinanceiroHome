package br.com.pinalli.financeirohome.dto;

import java.math.BigDecimal;

public record LimiteDisponivelResponse(
        Long cartaoId,
        String nome,
        String bandeiraCartao,
        BigDecimal limiteTotal,
        BigDecimal limiteDisponivel,
        BigDecimal totalComprasAbertas
) {
}