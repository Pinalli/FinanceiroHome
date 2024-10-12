package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.CartaoCredito;import br.com.pinalli.financeirohome.model.Usuario;

import lombok.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartaoCreditoDTO {

    private Long id;

    @NotNull(message = "A descrição não pode ser nula")
    @Size(min = 3, max = 100, message = "A descrição deve ter entre 3 e 100 caracteres")
    private String descricao;

    @NotNull(message = "O limite não pode ser nulo")
    @Positive(message = "O limite deve ser um valor positivo")
    private BigDecimal limite;

    @NotNull(message = "O valor não pode ser nulo")
    @PositiveOrZero(message = "O valor deve ser zero ou positivo")
    private BigDecimal valor;

    @NotNull(message = "O limite disponível não pode ser nulo")
    @PositiveOrZero(message = "O limite disponível deve ser zero ou positivo")
    private BigDecimal limiteDisponivel;

    @NotNull(message = "O total de compras abertas não pode ser nulo")
    @PositiveOrZero(message = "O total de compras abertas deve ser zero ou positivo")
    private BigDecimal totalComprasAbertas;

    // Adicionamos um getter para o usuarioId
    @Getter
    @NotNull(message = "O ID do usuário não pode ser nulo")
    private Long usuarioId;

    // Add a constructor that takes two BigDecimal parameters
    public CartaoCreditoDTO(BigDecimal limiteDisponivel, BigDecimal totalComprasAbertas) {
        this.limiteDisponivel = limiteDisponivel;
        this.totalComprasAbertas = totalComprasAbertas;
    }

    public CartaoCreditoDTO(Long id, String descricao, BigDecimal limite, BigDecimal limiteDisponivel, BigDecimal totalComprasAbertas) {
    }


    public static CartaoCreditoDTO fromEntity(CartaoCredito cartaoCredito) {
        return new CartaoCreditoDTO(
                cartaoCredito.getId(),
                cartaoCredito.getDescricao(),
                cartaoCredito.getLimite(),
                cartaoCredito.getValor(),
                cartaoCredito.getLimiteDisponivel(),
                cartaoCredito.getTotalComprasAbertas(),
                cartaoCredito.getUsuario().getId());
    }

    public static CartaoCreditoDTO converterParaDTO(CartaoCredito cartaoCredito) {
        if (cartaoCredito == null) return null; // Tratamento para cartaoCredito nulo

        return CartaoCreditoDTO.builder()
                .id(cartaoCredito.getId())
                .descricao(cartaoCredito.getDescricao())
                .limite(cartaoCredito.getLimite())
                .valor(cartaoCredito.getValor())
                .limiteDisponivel(cartaoCredito.getLimiteDisponivel())
                .totalComprasAbertas(cartaoCredito.getTotalComprasAbertas())
                .usuarioId(cartaoCredito.getUsuario().getId())
                .build();
    }
    public CartaoCredito toEntity() {
        CartaoCredito cartaoCredito = new CartaoCredito();
        cartaoCredito.setId(this.id);
        cartaoCredito.setDescricao(this.descricao);
        cartaoCredito.setLimite(this.limite);
        cartaoCredito.setValor(this.valor);
        cartaoCredito.setLimiteDisponivel(this.limiteDisponivel);
        cartaoCredito.setTotalComprasAbertas(this.totalComprasAbertas);

        // Criamos um objeto Usuario com o ID fornecido
        Usuario usuario = new Usuario();
        usuario.setId(this.usuarioId);
        cartaoCredito.setUsuario(usuario);

        return cartaoCredito;
    }


}