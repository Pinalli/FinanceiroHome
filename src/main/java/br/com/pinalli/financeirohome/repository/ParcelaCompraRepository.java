package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.ParcelaCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelaCompraRepository extends JpaRepository<ParcelaCompra, Long> {

    // Métodos personalizados, se necessário
}