package br.com.pinalli.financeirohome;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Arrays;
import java.util.Base64;


    public class JwtTest {
        public static void main(String[] args) {
            String secret = "21h46zaU6PQ1U5NYf/avsmTnAykZEu9XAK6Y8OQbsJU=";
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJGaW5hbmNlaXJvSG9tZSIsInN1YiI6Im1hcmlvLmJhbGFAZXhhbXBsZS5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE3MzcxNjg5NjksImV4cCI6MTczNzI1NTM2OX0.R2OyagYKGMYXQ9COoG4RS7001Kv_8FjqoYYx2QDLh2M";

            byte[] keyBytes = Base64.getDecoder().decode(secret);
            System.out.println("Chave secreta decodificada: " + Arrays.toString(keyBytes));

            try {
                Jws<Claims> claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                        .build()
                        .parseClaimsJws(token);
                System.out.println("Token é válido: " + claims);
            } catch (JwtException e) {
                System.out.println("Token inválido: " + e.getMessage());
            }
        }
    }
