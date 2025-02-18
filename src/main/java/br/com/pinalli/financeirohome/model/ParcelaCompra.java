package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "parcela_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParcelaCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    @NotNull
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private StatusParcela status = StatusParcela.PENDENTE;

    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    private CompraCartao compra;
}