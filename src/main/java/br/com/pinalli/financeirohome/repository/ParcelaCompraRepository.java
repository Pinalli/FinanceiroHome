package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.ParcelaCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParcelaCompraRepository extends JpaRepository<ParcelaCompra, Long> {
    List<ParcelaCompra> findByCompraId(Long compraId);
}