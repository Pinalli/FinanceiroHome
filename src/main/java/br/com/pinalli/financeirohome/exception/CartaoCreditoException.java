package br.com.pinalli.financeirohome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CartaoCreditoException extends RuntimeException {
    public CartaoCreditoException(String mensagem) {
        super(mensagem);
    }

    public CartaoCreditoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public CartaoCreditoException(Long id) {
        super("Cartão de crédito com ID " + id + " não encontrado.");
    }
}