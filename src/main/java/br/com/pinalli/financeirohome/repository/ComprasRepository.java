package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Compras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComprasRepository extends JpaRepository<Compras, Long> {

    List<Compras> findByCartaoCreditoId(Long cartaoId);

    // Métodos adicionais que podem ser úteis:
    List<Compras> findByCartaoCreditoIdAndDataBetween(Long cartaoId, LocalDate inicio, LocalDate fim);
    List<Compras> findByCartaoCreditoIdAndCategoriaIgnoreCase(Long cartaoId, String categoria);
}