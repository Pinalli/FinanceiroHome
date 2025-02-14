package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Categoria;
import br.com.pinalli.financeirohome.model.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByTipo(TipoCategoria tipo); // Busca por tipo

    List<Categoria> findByUsuarioId(Long usuarioId); // Busca por usuário

}
