package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Compras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComprasRepository extends JpaRepository<Compras, Long> {
    List<Compras> findByCartaoCreditoIdAndUsuarioId(Long cartaoCreditoId, Long usuarioId);
    Optional<Compras> findByIdAndUsuarioId(Long compraId, Long idUsuario);
}