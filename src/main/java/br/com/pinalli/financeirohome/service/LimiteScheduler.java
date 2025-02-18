package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LimiteScheduler {

    private final CartaoCreditoService cartaoCreditoService;
    private final CartaoCreditoRepository cartaoCreditoRepository;

    @Scheduled(cron = "0 0 0 * * *") // Executa diariamente à meia-noite
    public void atualizarLimitesDiariamente() {
        List<CartaoCredito> cartoes = cartaoCreditoRepository.findAll();
        cartoes.forEach(cartao ->
                cartaoCreditoService.atualizarLimiteDisponivel(cartao.getId()));
    }
}