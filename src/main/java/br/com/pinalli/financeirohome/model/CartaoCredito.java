package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cartao_credito")
public class CartaoCredito {
    // Getters e Setters
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    private String bandeiraCartao;


    @NotBlank
    private String numero;


    @Positive
    @Column(precision = 10, scale = 2)
    private BigDecimal limiteTotal;


    @PositiveOrZero
    @Column(precision = 10, scale = 2)
    private BigDecimal limiteDisponivel;


    @PositiveOrZero
    @Column(precision = 10, scale = 2)
    private BigDecimal totalComprasAbertas;


    @Min(1)
    @Max(31)
    private Integer  diaFechamento;

    @Min(1)
    @Max(31)
    private Integer  diaVencimento;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

}