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

    public static final Logger logger = LoggerFactory.getLogger(ContaPagarService.class);

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
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((authorize) -> authorize
                            .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/usuario/cadastro").permitAll()

                            //Usuário
                            .requestMatchers(HttpMethod.GET, "/api/usuario").hasRole("USER") // acesso restrito apenas para admin
                            .requestMatchers(HttpMethod.POST, "/api/usuario").hasRole("USER") // acesso restrito apenas para admin
                            .requestMatchers(HttpMethod.GET, "/api/usuario/{id}").hasRole("USER")
                            .requestMatchers(HttpMethod.PUT, "/api/usuario/{id}").hasRole("USER")
                            .requestMatchers(HttpMethod.DELETE, "/api/usuario/{id}").hasRole("USER")

                            //contas-a-pagar
                            .requestMatchers(HttpMethod.GET, "/api/contas-a-pagar/{id}").hasRole("USER")  // Correção: use **/
                            .requestMatchers(HttpMethod.POST, "/api/contas-a-pagar").hasRole("USER")  // Necessário
                            .requestMatchers(HttpMethod.PUT, "/api/contas-a-pagar/{id}").hasRole("USER")  // Necessário
                            .requestMatchers(HttpMethod.DELETE, "/api/contas-a-pagar/**").hasRole("USER")  // Necessário

                            //contas-a-receber
                            .requestMatchers(HttpMethod.GET, "/api/contas-a-receber/**").hasRole("USER")
                            .requestMatchers(HttpMethod.POST, "/api/contas-a-receber").hasRole("USER")
                            .requestMatchers(HttpMethod.PUT, "/api/contas-a-receber").hasRole("USER")
                            .requestMatchers(HttpMethod.DELETE, "/api/contas-a-receber/**").hasRole("USER")

                            //cartao-credito
                            .requestMatchers(HttpMethod.GET, "api/cartoes-credito/**").hasRole("USER")
                            .requestMatchers(HttpMethod.POST, "api/cartoes-credito").hasRole("USER")
                            .requestMatchers(HttpMethod.PUT,"api/cartoes-credito/{id}").hasRole("USER")
                            .requestMatchers(HttpMethod.PUT, "api/cartoes-credito").hasRole("USER")
                            .requestMatchers(HttpMethod.DELETE, "api/cartoes-credito/**").hasRole("USER")
                            .requestMatchers(HttpMethod.GET, "/api/cartoes-credito/usuario/**").hasRole("USER")
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



