package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.Conta;
import br.com.pinalli.financeirohome.model.StatusConta;
import br.com.pinalli.financeirohome.model.TipoConta;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContaResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        TipoConta tipo, // Tipo da conta (PAGAR ou RECEBER)
        LocalDate dataVencimento,
        StatusConta status,
        LocalDate dataPagamento,
        String observacao,
        Long usuarioId,
        String usuarioNome,
        Long categoriaId,
        String categoriaNome
) {
    // Metodo estático para converter Conta em ContaResponse
    public static ContaResponse fromConta(Conta conta) {
        return new ContaResponse(
                        conta.getId(),
                        conta.getDescricao(),
                        conta.getValor(),
                conta.getTipo(),
                        conta.getDataPagamento(),
                        conta.getStatus(),
                conta.getDataVencimento(),
                        conta.getObservacao(),
                        conta.getUsuario().getId(), // ID do usuário
                        conta.getUsuario().getNome(), // Nome do usuário
                        conta.getCategoria().getId(), // ID da categoria
                        conta.getCategoria().getNome() // Nome da categoria
                );
    }
}