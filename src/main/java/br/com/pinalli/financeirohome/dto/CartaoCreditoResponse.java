package br.com.pinalli.financeirohome.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CartaoCreditoResponse(
        Long id,
        String bandeiraCartao,
        String numero,

        @JsonProperty("dia_fechamento")
        Integer diaFechamento,

        @JsonProperty("dia_vencimento")
        Integer diaVencimento,

        @JsonProperty("limite_total")
        BigDecimal limiteTotal,

        @JsonProperty("limite_disponivel")
        BigDecimal limiteDisponivel,

        @JsonProperty("total_compras_abertas")
        BigDecimal totalComprasAbertas
) {}