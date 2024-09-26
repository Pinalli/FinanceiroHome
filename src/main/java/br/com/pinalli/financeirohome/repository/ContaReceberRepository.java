package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.ContaReceber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaReceberRepository extends JpaRepository<ContaReceber, Long> {
    Optional<ContaReceber> findByIdAndUsuarioId(Long id, Long idUsuario);
}