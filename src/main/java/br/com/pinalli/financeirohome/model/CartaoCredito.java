package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Entity
@Table(name = "cartao_credito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartaoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 100)
    private String bandeiraCartao;

    @NotBlank
    @Column(length = 20)
    private String numero;

    @Min(1)
    @Max(31)
    private Integer diaFechamento;

    @Min(1)
    @Max(31)
    private Integer diaVencimento;

    @DecimalMin("0.01")
    @Column(precision = 10, scale = 2)
    private BigDecimal limiteTotal;

    @DecimalMin("0.00")
    @Column(precision = 10, scale = 2)
    private BigDecimal limiteDisponivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}