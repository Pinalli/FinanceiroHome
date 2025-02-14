package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CategoriaDTO;
import br.com.pinalli.financeirohome.model.Categoria;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria criarCategoria(CategoriaDTO categoriaDTO, Usuario usuario) {
        Categoria categoria = new Categoria();
        categoria.setNome(categoriaDTO.getNome());
        categoria.setTipo(categoriaDTO.getTipo());
        categoria.setUsuario(usuario);
        return categoriaRepository.save(categoria);
    }
}