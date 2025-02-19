package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
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

    private BigDecimal valor;
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private StatusParcela status;

    @ManyToOne
    @JoinColumn(name = "compra_id")
    private CompraCartao compra;
}