package br.com.pinalli.financeirohome.repository;

import br.com.pinalli.financeirohome.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    // Você pode adicionar métodos de consulta personalizados aqui, se necessário
}