package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.StatusConta;
import br.com.pinalli.financeirohome.model.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataVencimento;

    @NotBlank(message = "Tipo da conta é obrigatório")
    @Pattern(regexp = "PAGAR|RECEBER", message = "Tipo inválido. Use 'PAGAR' ou 'RECEBER'")
    private String tipo;


    @Pattern(regexp = "PENDENTE|PAGA|RECEBIDA", message = "Status inválido")
    private String status;

    private Long usuarioId;
    private Long categoriaId;

    public ContaDTO(Long id, String descricao, BigDecimal valor, LocalDate data, String name, Long usuarioId, Long id1) {
    }
}
