package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compra_cartao")
public class CompraCartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.01", message = "O valor total deve ser maior que zero")
    private BigDecimal valorTotal;

    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @Column(nullable = false)
    private Boolean parcelado = false;

    @Column(name = "quantidade_parcelas", nullable = false)
    @Min(value = 1, message = "A quantidade de parcelas deve ser maior ou igual a 1")
    private Integer quantidadeParcelas;

    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    private CartaoCredito cartao;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<ParcelaCompra> parcelas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public BigDecimal getValor() {
        return valorTotal;
    }
}
