package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.LoginForm;
import br.com.pinalli.financeirohome.dto.TokenDTO;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import br.com.pinalli.financeirohome.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController .class);

    @Autowired
    private br.com.pinalli.financeirohome.service.TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AuthenticationManager authManager; // Declarar e injetar


    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@Valid @RequestBody LoginForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new TokenDTO(null,
                    bindingResult.getFieldError().
                            getDefaultMessage()));
        }

        UsernamePasswordAuthenticationToken dadosLogin = form.converter();

        try {
            Authentication authentication = authManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);
            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
        } catch (AuthenticationException e) {
            // Logar a tentativa de login malsucedida
            log.error("Tentativa de login malsucedida para o email: {}", form.getEmail());

            // Retornar uma mensagem de erro customizada
            return ResponseEntity.badRequest().body(new TokenDTO(null, "Email ou senha inválidos."));
        }
    }
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(UsuarioDTO::fromUsuario)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDTO);
    }

}
