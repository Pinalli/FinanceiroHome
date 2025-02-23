package br.com.pinalli.financeirohome.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CompraCartaoResponse(
        Long id,
        String descricao,
        BigDecimal valorTotal,
        Integer quantidadeParcelas,
        LocalDate dataCompra,
        Long cartaoId,
        String cartaoNome,
        Long categoriaId,
        String categoriaNome,
        List<ParcelaCompraResponse> parcelas
) {}
