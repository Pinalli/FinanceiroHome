package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.Compras;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComprasDTO {
    private Long id;
    private LocalDate data;
    private BigDecimal valor;
    private String descricao;
    private String categoria;
    private CartaoCreditoDTO cartaoCredito;
    private int parcelas;
    private int parcelasPagas;

    public static ComprasDTO fromEntity(Compras compra) {
        return new ComprasDTO(
                compra.getId(),
                compra.getData(),
                compra.getValor(),
                compra.getDescricao(),
                compra.getCategoria(),
                CartaoCreditoDTO.fromEntity(compra.getCartaoCredito()),
                compra.getParcelas(),
                compra.getParcelasPagas()
        );
    }

    public Compras toEntity() {
        Compras compra = new Compras();
        compra.setId(this.id);
        compra.setData(this.data);
        compra.setValor(this.valor);
        compra.setDescricao(this.descricao);
        compra.setCategoria(this.categoria);
        compra.setCartaoCredito(this.cartaoCredito.toEntity());
        compra.setParcelas(this.parcelas);
        compra.setParcelasPagas(this.parcelasPagas);
        return compra;
    }
}