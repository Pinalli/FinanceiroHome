package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Parcelas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelaRepository extends JpaRepository<Parcelas, Long> {
    // Métodos personalizados, se necessário
}