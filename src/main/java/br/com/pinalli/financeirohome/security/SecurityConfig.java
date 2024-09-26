package br.com.pinalli.financeirohome.security;

import br.com.pinalli.financeirohome.service.ContaPagarService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(ContaPagarService.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Retornar o AuthenticationManager diretamente
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF para APIs REST
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll() // Permite login sem autenticação
                        .requestMatchers(HttpMethod.POST, "/api/usuario/cadastro").permitAll() // Permite cadastro sem autenticação
                        .requestMatchers(HttpMethod.GET, "/api/contas-a-pagar/id").authenticated() // Requer autenticação
                        .requestMatchers(HttpMethod.GET, "/api/contas-a-pagar/usuario").authenticated() // Requer autenticação
                        .requestMatchers(HttpMethod.POST, "/api/contas-a-receber").hasRole("USER") // Apenas usuários com role "USER"
                        .requestMatchers(HttpMethod.GET, "/api/contas-a-receber/usuario").hasRole("USER") // Apenas usuários com role "USER"
                        .requestMatchers(HttpMethod.DELETE, "/api/contas-a-receber/{id}").hasRole("USER") // Apenas usuários com role "USER"
                        .requestMatchers(HttpMethod.GET, "/api/cartoes-credito/usuario/{usuarioId}").hasRole("USER") // Apenas usuários com role "USER"
                        .requestMatchers(HttpMethod.PUT, "/api/cartoes-credito/{id}").hasRole("USER") // Apenas usuários com role "USER"
                        .requestMatchers(HttpMethod.GET, "/api/cartoes-credito/{id}/limite-disponivel").hasRole("USER") // Apenas usuários com role "USER"
                        .requestMatchers(HttpMethod.POST, "/api/cartoes-credito//{cartaoId}/compras").hasRole("USER") // Apenas usuários com role "USER"
                        .anyRequest().authenticated() // Todos os outros endpoints exigem autenticação
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                            logger.error("Erro de autenticação: ", authException);
                            authException.printStackTrace(); // Adicione esta linha
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erro de autenticação");
                        })
                );

        return http.build();
    }

}
