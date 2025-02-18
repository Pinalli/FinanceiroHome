package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaRequest(
        @NotBlank String nome,
        @NotNull TipoCategoria tipo
) {}