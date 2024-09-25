package br.com.pinalli.financeirohome.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@Builder
public class ContaPagarDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private String status;
    private String categoria;

    private UsuarioDTO usuario;

    public ContaPagarDTO(Long id, String descricao, BigDecimal valor, LocalDate dataVencimento, String status, String categoria, UsuarioDTO usuario){
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.status = status;
        this.categoria = categoria;
        this.usuario = usuario;
    }

}