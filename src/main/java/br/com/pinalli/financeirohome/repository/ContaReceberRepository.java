package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.ContaReceber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaReceberRepository extends JpaRepository<ContaReceber, Long> {
    // Você pode adicionar métodos de consulta personalizados aqui, se necessário
}