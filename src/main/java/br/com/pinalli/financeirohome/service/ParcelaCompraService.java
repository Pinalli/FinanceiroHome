package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.CompraCartao;
import br.com.pinalli.financeirohome.model.ParcelaCompra;
import br.com.pinalli.financeirohome.model.StatusParcelaCompra;
import br.com.pinalli.financeirohome.repository.ParcelaCompraRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParcelaCompraService {


    private final ParcelaCompraRepository parcelaRepository;


    public ParcelaCompraService(ParcelaCompraRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;

    }

    public List<ParcelaCompra> criarParcelas(CompraCartao compra, int quantidadeParcelas) {
        // Validacao da quantidade de parcelas
        if (quantidadeParcelas <= 0) {
            throw new IllegalArgumentException("Quantidade de parcelas deve ser maior que zero.");
        }

        // Validação do valor total
        BigDecimal valorTotal = compra.getValorTotal();
        if (valorTotal == null) {
            throw new IllegalArgumentException("O valor total da compra não pode ser nulo.");
        }

        // Cálculo do valor da parcela
        BigDecimal valorParcela = valorTotal.divide(
                BigDecimal.valueOf(quantidadeParcelas),
                2, // Escala de 2 casas decimais
                RoundingMode.HALF_UP // Arredondamento
        );

        // Lista para armazenar as parcelas
        List<ParcelaCompra> parcelas = new ArrayList<>();

        // Geração das parcelas
        for (int i = 1; i <= quantidadeParcelas; i++) {
            ParcelaCompra parcela = new ParcelaCompra();
            parcela.setValor(valorParcela);
            parcela.setDataVencimento(
                    calcularDataVencimento(compra.getDataCompra(), i) // Método auxiliar
            );
            parcela.setStatus(StatusParcelaCompra.PENDENTE); // Enum definido
            parcela.setCompra(compra); // Relacionamento com a compra
            parcelas.add(parcela);
        }

        return parcelas;
    }

    // Metodo auxiliar para calcular a data de vencimento (exemplo)
    private LocalDate calcularDataVencimento(LocalDate dataCompra, int parcelaNumero) {
        return dataCompra.plusMonths(parcelaNumero)
                .withDayOfMonth(15); // Dia 15 de cada mês, por exemplo
    }
}