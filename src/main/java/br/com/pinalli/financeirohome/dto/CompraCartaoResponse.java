package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.CompraCartao;
import br.com.pinalli.financeirohome.model.ParcelaCompra;

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
        List<ParcelaResponse> parcelas
) {}
