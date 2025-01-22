package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.ContaPagar;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaPagarDTO {
    private Long id;
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "A data de vencimento é obrigatória")
    @Future(message = "A data de vencimento deve estar no futuro")
    private LocalDate dataVencimento;

    @NotBlank(message = "O status é obrigatório")
    private String status;

    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;

    @NotNull(message = "O usuário é obrigatório")
    private UsuarioDTO usuario;

    public ContaPagarDTO(ContaPagar entity) {
        //copy all attributes from entity to this
        BeanUtils.copyProperties(entity, this);

    }

}