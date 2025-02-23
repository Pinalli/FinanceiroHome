package br.com.pinalli.financeirohome.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CartaoCreditoRequest(
        @NotBlank String nome,
        @JsonProperty("bandeira_cartao")
        @NotBlank String bandeiraCartao,
        @NotBlank String numero,
        @JsonProperty("limite_total")
        @Positive BigDecimal limiteTotal,
        @JsonProperty("limite_disponivel")
        @PositiveOrZero BigDecimal limiteDisponivel,
        @JsonProperty("total_compras_abertas")
        @PositiveOrZero BigDecimal totalComprasAbertas,
        @JsonProperty("dia_fechamento")
        @Min(1) @Max(31) Integer diaFechamento,
        @JsonProperty("dia_vencimento")
        @Min(1) @Max(31) Integer diaVencimento
) {}