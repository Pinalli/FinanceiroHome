package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findByUsuarioIdAndTipo(Long usuarioId, Boolean tipo);
}