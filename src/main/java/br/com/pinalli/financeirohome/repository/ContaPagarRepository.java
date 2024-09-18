package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.ContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {
    // Você pode adicionar métodos de consulta personalizados aqui, se necessário
}