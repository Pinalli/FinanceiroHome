package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.LoginForm;
import br.com.pinalli.financeirohome.dto.TokenDTO;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;

import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import br.com.pinalli.financeirohome.service.ComprasService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(ComprasService.class); // For SLF4j

    @Autowired
    private br.com.pinalli.financeirohome.service.TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private  UsuarioRepository usuarioRepository;



    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@Valid @RequestBody LoginForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new TokenDTO(null,
                    Objects.requireNonNull(bindingResult.getFieldError()).
                            getDefaultMessage()));
        }

        UsernamePasswordAuthenticationToken dadosLogin = form.converter();

        try {
            Authentication authentication = authManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);
            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
        } catch (AuthenticationException e) {
            log.error("Tentativa de login malsucedida para o email: {}", form.getEmail());
            return ResponseEntity.badRequest().body(new TokenDTO(null, "Email ou senha inválidos."));
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO,
                                              @RequestParam String senha,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        try {
            usuarioService.cadastrarUsuario(usuarioDTO, senha);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id, Authentication authentication) {
        // Recupera o email do usuário autenticado
        String emailAutenticado = authentication.getName();

        // Busca o usuário pelo ID na base de dados
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Verifica se o email do usuário autenticado é o mesmo do usuário encontrado
            if (usuario.getEmail().equals(emailAutenticado)) {
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Retorna 403 Forbidden se o usuário tentar acessar dados de outro usuário
            }
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found se o usuário não for encontrado
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            return ResponseEntity.ok(usuarios); // Retorna a lista de usuários com status 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        try {
            Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);
            return ResponseEntity.ok(UsuarioDTO.fromUsuario(usuarioAtualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
        try {
            usuarioService.excluirUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
}
}
