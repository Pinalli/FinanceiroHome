package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;
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
    private UsuarioService usuarioService;

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
