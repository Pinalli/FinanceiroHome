package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.CartaoCredito;import br.com.pinalli.financeirohome.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    // Adicionamos um getter para o usuarioId
    @Getter
    @NotNull(message = "O ID do usuário não pode ser nulo")
    private Long usuarioId;


    public static CartaoCreditoDTO fromEntity(CartaoCredito cartaoCredito) {
        return new CartaoCreditoDTO(
                cartaoCredito.getId(),
                cartaoCredito.getDescricao(),
                cartaoCredito.getLimite(),
                cartaoCredito.getValor(),
                cartaoCredito.getUsuario().getId());
    }

    public CartaoCredito toEntity() {
        CartaoCredito cartaoCredito = new CartaoCredito();
        cartaoCredito.setId(this.id);
        cartaoCredito.setDescricao(this.descricao);
        cartaoCredito.setLimite(this.limite);
        cartaoCredito.setValor(this.valor);
        // Não setamos o usuário aqui, apenas mantemos o ID do usuário
        return cartaoCredito;
    }
    /* public CartaoCredito toEntity() {
        CartaoCredito cartaoCredito = new CartaoCredito();
        cartaoCredito.setId(this.id);
        cartaoCredito.setDescricao(this.descricao);
        cartaoCredito.setLimite(this.limite);
        cartaoCredito.setValor(this.valor);

        // Criamos um objeto Usuario com o ID fornecido
        Usuario usuario = new Usuario();
        usuario.setId(this.usuarioId);
        cartaoCredito.setUsuario(usuario);

        return cartaoCredito;
    }
*/

}