package br.com.pinalli.financeirohome.security;


import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

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
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        //login/cadastro
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuario/cadastro").permitAll()

                        //Usuário
                        .requestMatchers(HttpMethod.GET, "/api/usuario").hasRole("USER") // acesso restrito apenas para admin
                        .requestMatchers(HttpMethod.POST, "/api/usuario").hasRole("USER") // acesso restrito apenas para admin
                        .requestMatchers(HttpMethod.GET, "/api/usuario/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/usuario/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuario/{id}").hasRole("USER")

                        //contas-a-pagar
                        .requestMatchers(HttpMethod.GET, "/api/conta-a-pagar/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/conta-a-pagar").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/conta-a-pagar/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/conta-a-pagar/**").hasRole("USER")

                        //contas-a-receber
                        .requestMatchers(HttpMethod.GET, "/api/conta-a-receber/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/conta-a-receber").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/conta-a-receber").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/conta-a-receber/**").hasRole("USER")

                        //cartao-credito
                        .requestMatchers(HttpMethod.POST, "/api/cartoes-credito").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/cartoes-credito/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/cartoes-credito/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/cartoes-credito/{usuarioId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/cartoes-credito/usuario/{usuarioId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/cartoes-credito/usuario/{usuarioId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/cartoes-credito/{id}/limite").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/cartoes-credito/{usuarioId}/limite-compras-abertas").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/cartoes-credito/{id}/limite-e-compras").hasRole("USER")

                        // Compra Cartão de Crédito
                        .requestMatchers(HttpMethod.POST, "/api/compra/{cartaoId}/compra").hasRole("USER")
                        //  .requestMatchers(HttpMethod.GET, "/api/compras/{cartaoId}/compras").hasRole("USER") // listarComprasPorCartao
                        .requestMatchers(HttpMethod.GET, "/api/compra/{cartaoId}/compra/{compraId}").hasRole("USER") //buscarCompra

                        //CONTAS
                        .requestMatchers(HttpMethod.GET, "/api/contas/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/contas").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/contas/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/contas/**").hasRole("USER")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //SC_UNAUTHORIZED (401)
                        })
                );
        return http.build();
    }

}



