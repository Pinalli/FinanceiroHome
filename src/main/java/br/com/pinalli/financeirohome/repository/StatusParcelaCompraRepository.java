package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.StatusParcelaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StatusParcelaCompraRepository extends JpaRepository<StatusParcelaCompra, Long> {
    List<StatusParcelaCompra> findByParcela_Id(Long parcelaId);
    Optional<StatusParcelaCompra> findByNome(String nome);
}