package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartaoCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private Double limite;
    private Double valor;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}