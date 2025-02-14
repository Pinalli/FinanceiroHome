package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.StatusConta;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class StatusParcelaCompraDTO {

    private Long parcelaId;  // ID da ParcelaCompra
    private Long usuarioId;  // ID do usuário}
    private String status;  // Status da parcela (PENDENTE, PAGA, etc.)

    public StatusParcelaCompraDTO(Long id, String nome) {
    }

    public void setId(Long id) {
        this.parcelaId = id;
        this.usuarioId = id;
    }
}
