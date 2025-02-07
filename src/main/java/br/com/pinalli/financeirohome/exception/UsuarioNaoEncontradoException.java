package br.com.pinalli.financeirohome.exception;

public class UsuarioNaoEncontradoException extends RuntimeException { // Adicionado 'public'
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
}