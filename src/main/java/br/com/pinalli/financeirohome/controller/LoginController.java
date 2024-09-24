package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.LoginDTO;
import br.com.pinalli.financeirohome.dto.TokenDTO;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import br.com.pinalli.financeirohome.dto.LoginForm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.pinalli.financeirohome.service.TokenService ;

@RestController
@RequestMapping("/api")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthenticationManager authenticationManager; // Declarar e injetar
    @Autowired
    private TokenService tokenService;


    @PostMapping("/login")
    public ResponseEntity<String> autenticar(@RequestBody LoginForm form) {
        System.out.println("Método autenticar chamado!");
        System.out.println("Email: " + form.getEmail());
        System.out.println("Senha: " + form.getSenha());
        UsernamePasswordAuthenticationToken dadosLogin = form.converter();
        try {
            Authentication authentication = authenticationManager.authenticate(dadosLogin);
            System.out.println("Autenticação: " + authentication); // Log do objeto Authentication
            String token = tokenService.gerarToken(authentication);
            System.out.println("Token gerado: " + token); // Log do token gerado
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            System.out.println("Erro na autenticação: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
/**
    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new TokenDTO(null, bindingResult.getFieldError().getDefaultMessage()));
        }

        UsernamePasswordAuthenticationToken dadosLogin = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha());

        try {
            Authentication authentication = authManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);
            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
        } catch (AuthenticationException e) {
            // Logar a tentativa de login malsucedida
            log.error("Tentativa de login malsucedida para o email: {}", loginDTO.getEmail());

            // Retornar uma mensagem de erro customizada
            return ResponseEntity.badRequest().body(new TokenDTO(null, "Email ou senha inválidos."));
        }
    }*/
