package br.com.pinalli.financeirohome.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenService {

    @Getter
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsuarioEmail(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public String gerarToken(Authentication authentication) {
        UserDetails usuario = (UserDetails) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + expiration);

        byte[] keyBytes = Base64.getDecoder().decode(secret);
        System.out.println("Chave secreta para geração do token: " + Arrays.toString(keyBytes));

        String token = Jwts.builder()
                .setIssuer("FinanceiroHome")
                .setSubject(usuario.getUsername())
                .claim("roles", usuario.getAuthorities())
                .setIssuedAt(hoje)
                .setExpiration(dataExpiracao)
                .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
                .compact();

        System.out.println("Token gerado: " + token);
        return token;
    }

    public boolean isTokenValido(String token) {
        if (token == null) {
            System.out.println("Token é null");
            return false;
        }

        byte[] keyBytes = Base64.getDecoder().decode(secret);
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                    .build()
                    .parseClaimsJws(token);
            System.out.println("Claims do token: " + claims.getBody());
            return true;
        } catch (Exception e) {
            System.out.println("Erro específico na validação: " + e.getClass().getName());
            System.out.println("Mensagem de erro: " + e.getMessage());
            return false;
        }
    }
}
