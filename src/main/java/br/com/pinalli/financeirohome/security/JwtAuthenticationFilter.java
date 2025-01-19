package br.com.pinalli.financeirohome.security;

import br.com.pinalli.financeirohome.service.TokenService;
import io.jsonwebtoken.*;
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
import java.util.Base64;

@Component
@DependsOn("userDetailsServiceImpl")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String token = recuperarToken(request);
        System.out.println("\nToken recebido no filter: " + token );
        System.out.println("\nJwtAuthenticationFilter sendo executado para a URL: " + request.getRequestURI());


        System.out.println("\nToken extraído: " + token);

        if (token != null && tokenService.isTokenValido(token)) {
            System.out.println("\nToken válido!");

            try {
                String email = Jwts.parserBuilder()
                        .setSigningKey(Base64.getDecoder().decode(tokenService.getSecret()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();

                UserDetails usuario = userDetailsService.loadUserByUsername(email);
                System.out.println("Usuário autenticado: " + usuario.getUsername());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Usuário autenticado: " + authentication.getPrincipal());

            } catch (ExpiredJwtException e) {
                System.out.println("Token expirado: " + e.getMessage());
            } catch (MalformedJwtException e) {
                System.out.println("Token malformado: " + e.getMessage());
            } catch (JwtException e) {
                System.out.println("Erro de assinatura ou outro problema no token: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro ao processar o token JWT: " + e.getMessage());
            }
        } else {
            System.out.println("Token ausente ou inválido.");
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println("Token inválido ou ausente: " + token);
            return null;
        }

        return token.substring(7);
    }

}
