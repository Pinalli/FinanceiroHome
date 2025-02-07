package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartao_credito_id")
    private CartaoCredito cartaoCredito;

    @Column(nullable = false)
    private String categoria;

    @Column(name = "data_compra")
    private LocalDateTime dataCompra;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Integer parcelas;

    @Column(name = "parcelas_pagas", nullable = false)
    private Integer parcelasPagas;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCompra status;

    @Column(name = "valor_parcela", precision = 10, scale = 2)
    private BigDecimal valorParcela;

    @Column(name = "limite_disponivel_momento_compra", precision = 10, scale = 2)
    private BigDecimal limiteDisponivelMomentoCompra;

    @Column(name = "data_limite_compra")
    private LocalDateTime dataLimite;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ParcelaCompra> listaParcelas = new ArrayList<>();

    // Getter e Setter
    @Getter
    private BigDecimal valorTotal;


    public void adicionarParcela(ParcelaCompra parcela) {
        listaParcelas.add(parcela);
        parcela.setCompra(this);
    }

    public void removerParcela(ParcelaCompra parcela) {
        listaParcelas.remove(parcela);
        parcela.setCompra(null);
    }

    public enum StatusCompra {
        PENDENTE, PAGO, ABERTO
    }
}