package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.LoginForm;
import br.com.pinalli.financeirohome.service.TokenService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final AuthenticationManager authenticationManager; // Declarar e injetar
    private final TokenService tokenService;

    public LoginController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> autenticar(@Valid @RequestBody LoginForm form) {
        log.info("Tentativa de login para usuário: {}", form.getEmail());

        try {
            form.validate(); // validação adicional se necessário
            UsernamePasswordAuthenticationToken dadosLogin = form.converter();
            Authentication authentication = authenticationManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);

            log.info("Login bem-sucedido para usuário: {}", form.getEmail());
            return ResponseEntity.ok(token);

        } catch (AuthenticationException e) {
            log.error("Erro na autenticação para usuário: {}", form.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Falha na autenticação: credenciais inválidas");
        } catch (IllegalArgumentException e) {
            log.error("Dados de login inválidos para usuário: {}", form.getEmail(), e);
            return ResponseEntity.badRequest()
                    .body("Dados de login inválidos: " + e.getMessage());
        }
    }
}