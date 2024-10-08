package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.CartaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {
    List<CartaoCredito> findByUsuarioId(Long usuarioId);
    Optional<CartaoCredito> findById(Long id);
}