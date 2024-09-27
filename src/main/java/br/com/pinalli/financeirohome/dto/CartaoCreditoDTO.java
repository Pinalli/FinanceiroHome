package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.CartaoCredito;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartaoCreditoDTO {
    private Long id;
    private String descricao;
    private BigDecimal limite;
    private BigDecimal valor;

    public static CartaoCreditoDTO fromEntity(CartaoCredito cartaoCredito) {
        return new CartaoCreditoDTO(
                cartaoCredito.getId(),
                cartaoCredito.getDescricao(),
                cartaoCredito.getLimite(),
                cartaoCredito.getValor()
        );
    }

    public CartaoCredito toEntity() {
        CartaoCredito cartaoCredito = new CartaoCredito();
        cartaoCredito.setId(this.id);
        cartaoCredito.setDescricao(this.descricao);
        cartaoCredito.setLimite(this.limite);
        cartaoCredito.setValor(this.valor);
        return cartaoCredito;
    }
}