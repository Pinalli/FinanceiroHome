package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.CompraCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompraCartaoRepository extends JpaRepository<CompraCartao, Long> {
    List<CompraCartao> findByUsuarioId(Long usuarioId);
    List<CompraCartao> findByCartaoId(Long cartaoId);
}