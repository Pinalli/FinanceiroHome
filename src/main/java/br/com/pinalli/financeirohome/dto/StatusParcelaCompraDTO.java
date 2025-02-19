package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.StatusConta;

import java.time.LocalDateTime;


public record StatusParcelaCompraDTO (
        Long id, // ID do StatusParcelaCompra
        Long parcelaId, // ID da ParcelaCompra
        StatusConta status,  // Status da parcela (PENDENTE, PAGA, etc.)
        LocalDateTime dataAlteracao,  // Data e hora da alteração
        Long usuarioId  // ID do usuário

)
{} 