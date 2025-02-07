package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.StatusConta;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatusParcelasDTO {
    private Long id;
    private Long parcelaId;
    private StatusConta status; // Use StatusConta
    private LocalDateTime dataAlteracao;
    private Long usuarioId;
}



