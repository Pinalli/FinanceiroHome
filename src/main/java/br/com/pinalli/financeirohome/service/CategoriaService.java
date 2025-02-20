package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CategoriaRequest;
import br.com.pinalli.financeirohome.model.Categoria;
import br.com.pinalli.financeirohome.model.TipoCategoria;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;
    private final UsuarioService usuarioService;

    public Categoria criar(CategoriaRequest request, Usuario usuario) {
        Categoria categoria = new Categoria();
        categoria.setNome(request.nome());
        categoria.setTipo(request.tipo());
        categoria.setUsuario(usuario);
        return repository.save(categoria);
    }

    public List<Categoria> listarDisponiveis(TipoCategoria tipo, Usuario usuario) {
        return repository.findByTipoAndUsuarioOrUsuarioIsNull(tipo, usuario);
    }

    public Categoria buscarPorIdEUsuario(@NotNull Long id, Usuario usuario) {
        return (Categoria) repository.findByIdAndUsuario(id, usuario).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
    }
}