package br.com.pinalli.financeirohome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaPagarDTO {
    private Long id;
    private String descricao;
    private Double valor;
    private LocalDate dataVencimento;
    private String status;
    private String categoria;

}