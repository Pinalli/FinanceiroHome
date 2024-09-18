package br.com.pinalli.financeirohome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String senha = loginRequest.getSenha();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, senha);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            if (authentication.isAuthenticated()) {
                // Autenticacao realizada com sucesso
                return ResponseEntity.ok("Autenticado com sucesso!");
            } else {
                // Senha inválida
                return ResponseEntity.badRequest().body("Senha inválida");
            }
        } catch (BadCredentialsException e) {
            // Senha inválida
            return ResponseEntity.badRequest().body("Senha inválida");
        } catch (UsernameNotFoundException e) {
            // Usuário não encontrado
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        }
    }
}
