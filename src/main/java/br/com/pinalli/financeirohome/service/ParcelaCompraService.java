package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.exception.StatusNaoEncontradoException;
import br.com.pinalli.financeirohome.model.CompraCartao;
import br.com.pinalli.financeirohome.model.ParcelaCompra;
import br.com.pinalli.financeirohome.model.StatusParcela;
import br.com.pinalli.financeirohome.repository.ParcelaCompraRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParcelaCompraService {

    private final ParcelaCompraRepository parcelaRepository;

    public ParcelaCompraService(ParcelaCompraRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;
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

        // Usa o enum diretamente (não consulta mais o banco)
        StatusParcela statusPadrao = StatusParcela.PENDENTE;

        for (int i = 1; i <= quantidadeParcelas; i++) {
            ParcelaCompra parcela = ParcelaCompra.builder()
                    .numeroParcela(i)
                    .valor(valorParcela)
                    .dataVencimento(compra.getDataCompra().plusMonths(i))
                    .compra(compra)
                    .usuario(compra.getUsuario())
                    .status(statusPadrao) // Define o status via enum
                    .build();

            parcelas.add(parcela);
        }

        return parcelaRepository.saveAll(parcelas);
    }
}
