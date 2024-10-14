package br.com.pinalli.financeirohome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"usuario"})
@Table(name = "cartoes_credito")
public class CartaoCredito {

  //  @Setter
  //  @Getter
  //  private Integer numeroParcelas;
 //  @Setter
 //  @Getter
 //   private BigDecimal limiteCredito;

    @Id
    @Setter
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal limite;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Setter
    @Getter
    @Column(name="limite_disponivel", nullable = false, precision = 10, scale = 2)
    private BigDecimal limiteDisponivel;

    @Column(name = "total_compras_abertas",nullable = false, precision = 10, scale = 2)
    private BigDecimal totalComprasAbertas;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public void atualizarLimiteDisponivel(BigDecimal valorCompra) {
        this.limiteDisponivel = this.limiteDisponivel.subtract(valorCompra);
    }

    public void atualizarTotalComprasAbertas(BigDecimal valorCompra) {
        this.totalComprasAbertas = this.totalComprasAbertas.add(valorCompra);
    }
}