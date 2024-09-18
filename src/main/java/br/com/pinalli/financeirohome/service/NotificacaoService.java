package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.ContaPagar;
import br.com.pinalli.financeirohome.model.Notificacao;
import br.com.pinalli.financeirohome.model.TipoNotificacao;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public void criarNotificacaoEmail(Usuario usuario, ContaPagar conta) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setContaPagar(conta);
        notificacao.setTipo(TipoNotificacao.EMAIL);
        notificacao.setDataEnvio(LocalDate.now());
        notificacao.setDataVencimento(conta.getDataVencimento());
        notificacao.setStatus(false); // Assumindo que 'false' representa 'não lida'

        notificacaoRepository.save(notificacao);
    }
}