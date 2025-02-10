package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.StatusParcelaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StatusParcelaCompraRepository extends JpaRepository<StatusParcelaCompra, Long> {
    List<StatusParcelaCompra> findByParcela_Id(Long parcelaId);
}