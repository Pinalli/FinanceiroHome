package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.StatusConta;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaReceberDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataRecebimento;
    private StatusConta status;
    private String categoria;
    private UsuarioDTO usuarioDTO; // Usar o UsuarioDTO existente
}