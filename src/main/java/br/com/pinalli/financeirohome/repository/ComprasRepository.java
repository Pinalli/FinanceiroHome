package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Compras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ComprasRepository extends JpaRepository<Compras, Long> {
    List<Compras> findByCartaoCreditoIdAndUsuarioId(Long cartaoCreditoId, Long usuarioId);

    Optional<Compras> findByIdAndUsuarioId(Long compraId, Long idUsuario);

  //  @Query("SELECT c FROM Compras c WHERE c.cartaoCredito.id = :cartaoCreditoId AND c.status = :status")
   // List<Compras> findComprasByCartaoCreditoIdAndStatus(@Param("cartaoCreditoId") Long cartaoCreditoId, @Param("status") String status);

}