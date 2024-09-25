package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.StatusConta;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContaReceberDTO {


    private Long id;

    @NotBlank(message = "A descrição não pode estar vazia")
    @Size(max = 255, message = "A descrição não pode ter mais de 255 caracteres")
    private String descricao;

    @NotNull(message = "O valor não pode ser nulo")
    @Positive(message = "O valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "A data de recebimento não pode ser nula")
    @FutureOrPresent(message = "A data de recebimento deve ser no presente ou no futuro")
    private LocalDate dataRecebimento;

    @NotNull(message = "O status não pode ser nulo")
    private StatusConta status;

    @Size(max = 50, message = "A categoria não pode ter mais de 50 caracteres")
    private String categoria;


    private UsuarioDTO usuarioDTO;
    // Novo campo opcional
    private Long usuarioId;

}