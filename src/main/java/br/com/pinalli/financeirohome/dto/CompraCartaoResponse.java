package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.CompraCartao;
import com.fasterxml.jackson.annotation.JsonFormat;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CompraCartaoResponse(
        Long id, // Long
        String descricao, // String
        BigDecimal valorTotal, // BigDecimal
        int quantidadeParcelas, // int
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dataCompra, // LocalDate
        Long cartaoId, // Long
        String observacao,
        // String
        Long categoriaId, // Long
        String categoriaNome, // String
        List<ParcelaResponse> parcelas // Lista de ParcelaResponse
) {
    public static CompraCartaoResponse fromCompraCartao(CompraCartao compraSalva) {
        return new CompraCartaoResponse(
                compraSalva.getId(),
                compraSalva.getDescricao(),
                compraSalva.getValorTotal(),
                compraSalva.getQuantidadeParcelas(),
                compraSalva.getDataCompra(),
                compraSalva.getCartao().getId(),
                compraSalva.getCartao().getBandeiraCartao(),
                compraSalva.getCategoria().getId(),
                compraSalva.getCategoria().getNome(),
                compraSalva.getParcelas().stream()
                        .map(ParcelaResponse::fromParcelaCompra)
                        .toList()
        );
    }
}