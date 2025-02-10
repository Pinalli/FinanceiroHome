package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.TipoCategoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    private Long id;
    private String nome;
    private TipoCategoria tipo;

    public CategoriaDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
