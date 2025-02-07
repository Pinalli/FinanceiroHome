package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "contas")
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private BigDecimal valor;
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private StatusConta status;

    private String categoria;

    @Column(nullable = false)
    private boolean tipo; // true = conta a pagar, false = conta a receber

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}