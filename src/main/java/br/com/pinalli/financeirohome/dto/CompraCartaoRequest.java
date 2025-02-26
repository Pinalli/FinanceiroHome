package br.com.pinalli.financeirohome.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CompraCartaoRequest(
        @NotBlank(message = "A descrição não pode estar em branco") String descricao,
        @Positive(message = "O valor total deve ser um valor positivo") BigDecimal valorTotal,
        @Min(value = 1, message = "A quantidade de parcelas deve ser pelo menos 1") int quantidadeParcelas,
        @NotNull(message = "A data da compra não pode ser nula") LocalDate dataCompra, // Adicionado
        String observacao,
        @NotNull(message = "O ID do cartão não pode ser nulo") Long cartaoId,
        @NotNull(message = "O ID da categoria não pode ser nulo") Long categoriaId
) {
}