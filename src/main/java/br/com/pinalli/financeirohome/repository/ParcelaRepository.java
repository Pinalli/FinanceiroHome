package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
    // Métodos personalizados, se necessário
}