package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


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

    @Column(nullable = false)
    private Integer parcelas;

    @Column(name = "parcelas_pagas", nullable = false)
    private Integer parcelasPagas;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "valor_parcela", precision = 10, scale = 2)
    private BigDecimal valorParcela;

    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusCompra status;

    @Column(name = "limite_disponivel_momento_compra", precision = 10, scale = 2)
    private BigDecimal limiteDisponivelMomentoCompra;

    public enum StatusCompra {
        PENDENTE, PAGO
    }

}