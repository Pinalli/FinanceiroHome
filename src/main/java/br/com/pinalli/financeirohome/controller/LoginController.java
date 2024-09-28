package br.com.pinalli.financeirohome.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import br.com.pinalli.financeirohome.dto.LoginForm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
