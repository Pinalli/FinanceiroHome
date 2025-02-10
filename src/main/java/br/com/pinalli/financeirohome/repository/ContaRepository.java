package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Conta;
import br.com.pinalli.financeirohome.model.TipoConta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findByTipo(TipoConta tipo);

}