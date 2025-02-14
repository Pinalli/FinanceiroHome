package br.com.pinalli.financeirohome.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @Positive(message = "Valor total deve ser positivo")
    private BigDecimal valorTotal;

    @NotNull(message = "Data da compra é obrigatória")
    private LocalDate dataCompra;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;

    private boolean parcelado;

    @Min(value = 1, message = "Quantidade de parcelas deve ser >= 1")
    private Integer quantidadeParcelas;

    @NotNull(message = "ID do cartão é obrigatório")
    private Long cartaoId;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
}
