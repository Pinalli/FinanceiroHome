package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoCategoria tipo; // Enum: DESPESA, RECEITA, CARTAO

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
