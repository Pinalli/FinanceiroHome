package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Getter
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String gerarToken(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + expiration);

        return Jwts.builder()
                .setIssuer("FinanceiroHome")
                .setSubject(usuario.getEmail())
                .setIssuedAt(hoje)
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isTokenValido(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}