package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findByTipo(boolean tipo); // Busca contas por tipo (pagar/receber)
}