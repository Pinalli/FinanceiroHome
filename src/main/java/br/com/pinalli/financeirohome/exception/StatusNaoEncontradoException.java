package br.com.pinalli.financeirohome.exception;


public class StatusNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StatusNaoEncontradoException(String message) {
        super(message);
    }

    public StatusNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
