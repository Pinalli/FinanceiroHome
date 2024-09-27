package br.com.pinalli.financeirohome.exception;

// Exceção para cartão de crédito não encontrado
public class CartaoCreditoNotFoundException extends RuntimeException {
    public CartaoCreditoNotFoundException(Long cartaoId) {
        super("Cartão de crédito com ID " + cartaoId + " não foi encontrado.");
    }
}