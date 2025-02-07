package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.StatusParcelas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StatusParcelaRepository extends JpaRepository<StatusParcelas, Long> {
    List<StatusParcelas> findByParcela_Id(Long parcelaId);
}