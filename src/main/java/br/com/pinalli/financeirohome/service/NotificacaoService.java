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


}