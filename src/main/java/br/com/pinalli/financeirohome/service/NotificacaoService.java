package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.*;
import br.com.pinalli.financeirohome.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;


    public void criarNotificacao(Usuario usuario, ContaPagar conta, TipoNotificacao tipo) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setContaPagar(conta);
        notificacao.setTipo(tipo);
        notificacao.setDataEnvio(LocalDate.now());
        notificacao.setDataVencimento(conta.getDataVencimento());
        notificacao.setStatus(false); // Assumindo que 'false' representa 'não lida'

        notificacaoRepository.save(notificacao);
    }

    public void criarNotificacao(Usuario usuario, ContaReceber conta, TipoNotificacao tipo) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setContaReceber(conta); // Definir a conta a receber
        notificacao.setTipo(tipo);
        notificacao.setDataEnvio(LocalDate.now());
        notificacao.setDataVencimento(conta.getDataRecebimento());
        notificacao.setStatus(false);

        notificacaoRepository.save(notificacao);
    }
}