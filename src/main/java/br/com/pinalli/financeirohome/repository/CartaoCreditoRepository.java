package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.CartaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {

    @Query(value = "SELECT limite_total - COALESCE(SUM(c.valor_total), 0) FROM cartao_credito cc " +
            "LEFT JOIN compra_cartao c ON cc.id = c.cartao_id WHERE cc.id = ?1",
            nativeQuery = true)
    BigDecimal calcularLimiteDisponivel(Long cartaoId);

    @Modifying
    @Query("UPDATE CartaoCredito cc SET cc.limiteDisponivel = :novoLimite WHERE cc.id = :cartaoId")
    void atualizarLimiteDisponivel(@Param("cartaoId") Long cartaoId,
                                   @Param("novoLimite") BigDecimal novoLimite);
}