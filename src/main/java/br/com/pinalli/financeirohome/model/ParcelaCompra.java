package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "parcela_compra")
@Entity
public class ParcelaCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numeroParcela; // Ex: 1, 2, 3...

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor; // Valor da parcela

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento; // Data de vencimento

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusParcelaCompra status; // Status (PENDENTE, PAGA, etc.)

    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    private CompraCartao compra; // Compra associada

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // Usuário dono da parcela
}