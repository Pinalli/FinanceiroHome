package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CategoriaDTO;
import br.com.pinalli.financeirohome.model.Categoria;
import br.com.pinalli.financeirohome.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> criarCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaService.criarCategoria(categoriaDTO);
        return ResponseEntity.ok(new CategoriaDTO(categoria.getId(), categoria.getNome(), categoria.getTipo()));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        List<CategoriaDTO> categorias = categoriaService.listarCategorias().stream()
                .map(c -> new CategoriaDTO(c.getId(), c.getNome(), c.getTipo()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(new CategoriaDTO(categoria.getId(), categoria.getNome(), categoria.getTipo()));
    }
}
