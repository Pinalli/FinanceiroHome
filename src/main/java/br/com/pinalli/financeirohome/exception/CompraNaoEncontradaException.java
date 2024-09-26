package br.com.pinalli.financeirohome.exception;

// Exceção para compra não encontrada
public class CompraNaoEncontradaException extends RuntimeException {
    public CompraNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}