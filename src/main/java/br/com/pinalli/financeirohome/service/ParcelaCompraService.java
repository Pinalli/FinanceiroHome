package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.exception.StatusNaoEncontradoException;
import br.com.pinalli.financeirohome.model.CompraCartao;
import br.com.pinalli.financeirohome.model.ParcelaCompra;
import br.com.pinalli.financeirohome.repository.ParcelaCompraRepository;
import br.com.pinalli.financeirohome.repository.StatusParcelaCompraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.pinalli.financeirohome.model.StatusParcelaCompra;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParcelaCompraService {


    private final ParcelaCompraRepository parcelaRepository;
    private final StatusParcelaCompraRepository statusParcelaCompraRepository;

    public ParcelaCompraService(ParcelaCompraRepository parcelaRepository, StatusParcelaCompraRepository statusParcelaCompraRepository) {
        this.parcelaRepository = parcelaRepository;
        this.statusParcelaCompraRepository = statusParcelaCompraRepository;
    }

    @Transactional
    public List<ParcelaCompra> criarParcelas(CompraCartao compra, int quantidadeParcelas) {
        if (quantidadeParcelas <= 0) {
            throw new IllegalArgumentException("Quantidade de parcelas deve ser maior que zero.");
        }

        BigDecimal valorTotal = compra.getValorTotal();
        if (valorTotal == null) {
            throw new IllegalArgumentException("O valor total da compra não pode ser nulo.");
        }

        List<ParcelaCompra> parcelas = new ArrayList<>();
        BigDecimal valorParcela = valorTotal.divide(
                BigDecimal.valueOf(quantidadeParcelas),
                2,
                RoundingMode.HALF_UP
        );

        StatusParcelaCompra statusPendente = statusParcelaCompraRepository
                .findByNome("PENDENTE")
                .orElseThrow(() -> new StatusNaoEncontradoException("Status 'PENDENTE' não encontrado"));

        for (int i = 1; i <= quantidadeParcelas; i++) {
            ParcelaCompra parcela = ParcelaCompra.builder()
                    .numeroParcela(i)
                    .valor(valorParcela)
                    .dataVencimento(compra.getDataCompra().plusMonths(i))
                    .compra(compra)
                    .usuario(compra.getUsuario())
                    .status(statusPendente)
                    .build();

            parcelas.add(parcela);
        }

        return parcelaRepository.saveAll(parcelas);
    }

}