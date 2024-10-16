package br.com.pinalli.financeirohome.exception;

public class ParcelaNotFoundException extends RuntimeException {
    public ParcelaNotFoundException(String message) {
        super(message);
    }
}