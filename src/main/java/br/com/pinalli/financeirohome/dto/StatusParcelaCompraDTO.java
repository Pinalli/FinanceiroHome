package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.StatusConta;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatusParcelaCompraDTO {
    private Long id;  // ID do StatusParcelaCompra
    private Long parcelaId;  // ID da ParcelaCompra
    private StatusConta status;  // Status da parcela (PENDENTE, PAGA, etc.)
    private LocalDateTime dataAlteracao;  // Data e hora da alteração
    private Long usuarioId;  // ID do usuário
}