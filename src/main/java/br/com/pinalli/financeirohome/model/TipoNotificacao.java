package br.com.pinalli.financeirohome.model;

public enum TipoNotificacao {
    EMAIL(0), // Valor 0 para EMAIL
    SMS(1);   // Valor 1 para SMS
    // ... outros tipos de notificação

    private final int valor;

    TipoNotificacao(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}