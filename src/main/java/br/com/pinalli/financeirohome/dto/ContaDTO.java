package br.com.pinalli.financeirohome.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ContaDTO {
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private boolean tipo; // true = pagar, false = receber
    private String categoria;
    private Long usuarioId;

    public ContaDTO(Long id, String descricao, boolean tipo) {
    }

    public boolean getTipo() {
        if (this.tipo) {
            return true;
        }
        if (!this.tipo) {
            return false;
        }
        else {
            return false;
        }

    }
}