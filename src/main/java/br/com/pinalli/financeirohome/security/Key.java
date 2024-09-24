package br.com.pinalli.financeirohome.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class Key {
    public static void main(String[] args) {
        byte[] chave = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        String chaveSecreta = Base64.getEncoder().encodeToString(chave);
        System.out.println("Chave secreta JWT: " + chaveSecreta);
    }
}