package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CategoriaRequest;
import br.com.pinalli.financeirohome.model.Categoria;
import br.com.pinalli.financeirohome.model.TipoCategoria;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.service.CategoriaService;
import br.com.pinalli.financeirohome.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody CategoriaRequest request, Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        Categoria categoria = categoriaService.criar(request, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listar(
            @RequestParam TipoCategoria tipo,
            Principal principal
    ) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        return ResponseEntity.ok(categoriaService.listarDisponiveis(tipo, usuario));
    }
}