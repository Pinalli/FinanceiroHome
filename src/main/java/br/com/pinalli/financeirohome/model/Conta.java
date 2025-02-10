package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private TipoConta tipo; // Corrigido para Enum

    @Enumerated(EnumType.STRING)
    private StatusConta status;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

  //  @Column(nullable = false)
   // private boolean tipo; // true = conta a pagar, false = conta a receber
    private boolean recorrente;

    private String periodicidade;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    private LocalDateTime dataAtualizacao;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}