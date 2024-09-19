package br.com.pinalli.financeirohome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioCompletoDTO {
    private List<ContaPagarDTO> contasPagar;
   // private List<ContaReceberDTO> contasReceber;
  //  private List<CartaoDTO> cartoes;
    private Double saldoTotal;
    private Double totalDespesas;
    private Double totalReceitas;
    // ... outros campos calculados ...
}