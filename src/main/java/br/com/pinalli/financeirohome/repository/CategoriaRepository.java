package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Categoria;
import br.com.pinalli.financeirohome.model.TipoCategoria;
import br.com.pinalli.financeirohome.model.Usuario;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByTipoAndUsuarioOrUsuarioIsNull(TipoCategoria tipo, Usuario usuario);
    Optional<Categoria> findByIdAndUsuario(@NotNull Long aLong, Usuario usuario);
}

