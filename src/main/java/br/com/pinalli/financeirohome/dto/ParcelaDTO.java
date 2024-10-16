package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.Parcela;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ParcelaDTO {
    private Long id;
    private BigDecimal valorParcela;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private Parcela.StatusParcela status;
}