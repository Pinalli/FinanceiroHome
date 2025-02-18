package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.LimiteCartaoView;
import br.com.pinalli.financeirohome.repository.LimiteCartaoViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final LimiteCartaoViewRepository limiteCartaoViewRepository;

    public BigDecimal getLimiteDisponivel(Long cartaoId) {
        return limiteCartaoViewRepository.findById(cartaoId)
                .map(LimiteCartaoView::getLimiteDisponivel)
                .orElse(BigDecimal.ZERO);
    }
}