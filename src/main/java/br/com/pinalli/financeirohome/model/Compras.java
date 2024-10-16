package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartao_credito_id", nullable = false)
    private CartaoCredito cartaoCredito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String categoria;

    @Column(name = "data_compra", nullable = false)
    private LocalDateTime dataCompra;

    @Column(nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<Parcela> parcelas = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;


    @Setter
    @Getter
    @Column(name = "valor_parcela", precision = 10, scale = 2)
    private BigDecimal valorParcela;

    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusCompra status;

    @Setter
    @Getter
    @Column(name = "limite_disponivel_momento_compra", precision = 10, scale = 2)
    private BigDecimal limiteDisponivelMomentoCompra;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;




    public void calcularValorTotal() {
        valorTotal = parcelas.stream()
                .map(Parcela::getValorParcela)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public void adicionarParcela(Parcela parcela) {
        parcelas.add(parcela);
        parcela.setCompra(this);
    }

    public void removerParcela(Parcela parcela) {
        parcelas.remove(parcela);
        parcela.setCompra(null);
    }

    public enum StatusCompra {
        PENDENTE, PAGO

    }

}