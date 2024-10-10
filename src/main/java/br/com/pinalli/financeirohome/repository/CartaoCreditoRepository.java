package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {
    List<CartaoCredito> findByUsuarioId(Long usuarioId);
    Optional<CartaoCredito> findById(Long id);


    @Query("SELECT c.limiteDisponivel, c.totalComprasAbertas FROM CartaoCredito c WHERE c.usuario.id = :usuarioId")
    List<Object[]> getLimiteDisponivelAndTotalComprasAbertasByUsuarioId(@Param("usuarioId") Long usuarioId);
}