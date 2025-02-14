package br.com.pinalli.financeirohome.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@Data
public class CompraCartaoDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataCompra;
    private String categoria;
    private Integer parcelas;
    private Long cartaoId;
    private Long usuarioId;
}
