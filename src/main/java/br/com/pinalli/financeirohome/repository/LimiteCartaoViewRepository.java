package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.LimiteCartaoView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LimiteCartaoViewRepository extends JpaRepository<LimiteCartaoView, Long> {

    // Consulta direta pelo ID do cartão
    Optional<LimiteCartaoView> findById(Long cartaoId);
}