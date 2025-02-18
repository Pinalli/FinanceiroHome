package br.com.pinalli.financeirohome.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compra_cartao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraCartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String descricao;

    @Positive
    @Column(precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Min(1)
    private Integer quantidadeParcelas;

    @NotNull
    private LocalDate dataCompra;

    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    private CartaoCredito cartao;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParcelaCompra> parcelas = new ArrayList<>();
}