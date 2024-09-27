package br.com.pinalli.financeirohome.exception;

// Exceção para compra não encontrada
public class CompraNotFoundException extends RuntimeException {
    public CompraNotFoundException(Long compraId) {
        super("Compra com ID " + compraId + " não foi encontrada.");
    }
}