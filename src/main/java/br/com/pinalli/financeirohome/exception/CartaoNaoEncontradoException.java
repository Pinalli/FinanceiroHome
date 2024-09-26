package br.com.pinalli.financeirohome.exception;

// Exceção para cartão não encontrado
public class CartaoNaoEncontradoException extends RuntimeException {
    public CartaoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}