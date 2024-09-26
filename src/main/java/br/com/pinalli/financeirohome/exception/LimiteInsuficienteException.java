package br.com.pinalli.financeirohome.exception;

// Exceção para limite insuficiente
public class LimiteInsuficienteException extends RuntimeException {
    public LimiteInsuficienteException(String mensagem) {
        super(mensagem);
    }
}