package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.StatusConta;
import br.com.pinalli.financeirohome.model.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private TipoConta tipo;
    private StatusConta status;
    private Long categoriaId;
    private boolean recorrente;
    private String periodicidade;
    private String observacao;
    private Long usuarioId;
}
