package br.com.pinalli.financeirohome.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaDTO {
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private boolean tipo; // true = pagar, false = receber
    private String categoria;
    private Long usuarioId;
}