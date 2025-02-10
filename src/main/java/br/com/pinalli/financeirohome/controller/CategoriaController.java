package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CategoriaDTO;
import br.com.pinalli.financeirohome.model.Categoria;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<Categoria> criarCategoria(@RequestBody CategoriaDTO categoriaDTO,
                                                    Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        Categoria novaCategoria = categoriaService.criarCategoria(categoriaDTO, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }
}
