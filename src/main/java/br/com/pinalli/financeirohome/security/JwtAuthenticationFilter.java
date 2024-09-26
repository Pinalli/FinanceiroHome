package br.com.pinalli.financeirohome.security;

import br.com.pinalli.financeirohome.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        if (request.getRequestURI().equals("/api/login") ||
                request.getRequestURI().equals("/api/usuario/cadastro")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = recuperarToken(request);
        System.out.println("Token extraído: " + token);

        if (token != null && tokenService.isTokenValido(token)) {
            System.out.println("Token válido!");

            try {
                String email = Jwts.parserBuilder()
                        .setSigningKey(tokenService.getSecret().getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();

                UserDetails usuario = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Usuário autenticado: " + authentication.getPrincipal());

            } catch (ExpiredJwtException e) {
                System.out.println("Token expirado: " + e.getMessage());
            } catch (MalformedJwtException e) {
                System.out.println("Token malformado: " + e.getMessage());
            } catch (SignatureException e) {
                System.out.println("Assinatura do token inválida: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Token JWT está vazio ou inválido: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro ao processar o token JWT: " + e.getMessage());
            }
            filterChain.doFilter(request, response);
        }
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}