package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.TokenDTO;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;
import br.com.pinalli.financeirohome.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private br.com.pinalli.financeirohome.service.TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager; // Injetar o AuthenticationManager



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO usuarioDTO) {
        UsernamePasswordAuthenticationToken dadosLogin = usuarioDTO.converter();
        try {
            Authentication authentication = authenticationManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);

            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Erro ao autenticar usuário");
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            usuarioService.cadastrarUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
