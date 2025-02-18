package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 50)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private TipoCategoria tipo; // Enum: DESPESA, RECEITA

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // NULL para categorias padrão
}