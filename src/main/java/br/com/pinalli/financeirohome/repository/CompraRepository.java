package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByCartaoCreditoId(Long cartaoCreditoId);
}