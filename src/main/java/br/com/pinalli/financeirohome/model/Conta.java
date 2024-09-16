package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private Double valor;
    private Boolean tipo; // true = conta a pagar, false = conta a receber
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}