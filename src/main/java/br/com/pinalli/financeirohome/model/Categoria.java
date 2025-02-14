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

    @Column(name = "nome", nullable = false) // Nome do campo no banco
    private String nome; // Nome do campo na entidade

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCategoria tipo; // Ex: DESPESA, RECEITA, CARTAO

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
