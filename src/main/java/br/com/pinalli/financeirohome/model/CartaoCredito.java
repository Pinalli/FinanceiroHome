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
@Table(name = "cartao_credito")
public class CartaoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    @Column(name="bandeira_cartao",nullable = false, length = 100)
    private String bandeiraCartao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal limite;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name="limite_disponivel", nullable = false, precision = 10, scale = 2)
    private BigDecimal limiteDisponivel;

    @Column(name = "total_compras_abertas",nullable = false, precision = 10, scale = 2)
    private BigDecimal totalComprasAbertas;


    public void atualizarLimiteDisponivel(BigDecimal valorCompra) {
        this.limiteDisponivel = this.limiteDisponivel.subtract(valorCompra);
    }

    public void atualizarTotalComprasAbertas(BigDecimal valorCompra) {
        this.totalComprasAbertas = this.totalComprasAbertas.add(valorCompra);
    }

    public CartaoCredito toEntity() {
        return CartaoCredito.builder()
                .id(this.id)
                .usuario(this.usuario)
                .bandeiraCartao(this.bandeiraCartao)
                .limite(this.limite)
                .valor(this.valor)
                .limiteDisponivel(this.limiteDisponivel)
                .totalComprasAbertas(this.totalComprasAbertas)
                .build();
    }
}