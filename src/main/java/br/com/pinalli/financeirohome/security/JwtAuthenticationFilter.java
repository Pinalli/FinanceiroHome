package br.com.pinalli.financeirohome.security;

import br.com.pinalli.financeirohome.service.TokenService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.context.annotation.DependsOn;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@DependsOn("userDetailsServiceImpl") // Substitua pelo nome do seu bean UserDetailsService
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("JwtAuthenticationFilter sendo executado para a URL: " + request.getRequestURI());

        // Ignorar a requisição de login
        if (request.getRequestURI().equals("/api/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = recuperarToken(request);
        System.out.println("Token extraído: " + token);

        if (token != null && tokenService.isTokenValido(token)) {
            System.out.println("Token válido!");

            // Extrair o email do token JWT
            String email = Jwts.parser()
                    .setSigningKey(tokenService.getSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            UserDetails usuario = userDetailsService.loadUserByUsername(email);

            // Criar o UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Definir o Authentication no SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Usuário autenticado: " + authentication.getPrincipal());
        }

        filterChain.doFilter(request, response);

        // ... outros métodos ...
    }



    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7); // remove "Bearer "
    }
}