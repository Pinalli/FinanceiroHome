package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.CompraCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompraCartaoRepository extends JpaRepository<CompraCartao, Long> {
    Optional<CompraCartao> findByCartaoIdAndId(Long cartaoId, Long id);
    List<CompraCartao> findByCartaoId(Long cartaoId);

    }
