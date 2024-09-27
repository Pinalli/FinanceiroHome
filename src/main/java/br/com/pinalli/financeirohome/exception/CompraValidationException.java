package br.com.pinalli.financeirohome.exception;

// Exceção para qualquer erro relacionado a validação
public class CompraValidationException extends RuntimeException {
    public CompraValidationException(String message) {
        super(message);
    }
}