package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.ContaPagar;
import br.com.pinalli.financeirohome.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {
    // Defina o método aqui
    List<ContaPagar> findAllByUsuario(Usuario usuario);
}