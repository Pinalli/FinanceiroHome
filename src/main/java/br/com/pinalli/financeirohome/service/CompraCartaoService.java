package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CompraCartaoRequest;
import br.com.pinalli.financeirohome.dto.CompraCartaoResponse;
import br.com.pinalli.financeirohome.dto.ParcelaResponse;
import br.com.pinalli.financeirohome.exception.CategoriaInvalidaException;
import br.com.pinalli.financeirohome.model.*;
import br.com.pinalli.financeirohome.repository.CompraCartaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompraCartaoService {

    private final CompraCartaoRepository compraCartaoRepository;
    private final CartaoCreditoService cartaoService;
    private final CategoriaService categoriaService;

    public CompraCartaoResponse criarCompra(CompraCartaoRequest request, Usuario usuario) {
        CartaoCredito cartao = cartaoService.buscarPorIdEUsuario(request.cartaoId(), usuario);
        Categoria categoria = categoriaService.buscarPorIdEUsuario(request.categoriaId(), usuario);
        validarCategoria(categoria);

        // Cria compra
        CompraCartao compra = new CompraCartao();
        compra.setDescricao(request.descricao());
        compra.setValorTotal(request.valorTotal());
        compra.setQuantidadeParcelas(request.quantidadeParcelas());
        compra.setDataCompra(LocalDate.now());
        compra.setCartao(cartao);
        compra.setCategoria(categoria);
        compra.setUsuario(usuario);

        // Gera parcelas
        List<ParcelaCompra> parcelas = gerarParcelas(compra, cartao.getDiaVencimento());
        compra.setParcelas(parcelas);

        // Salva e retorna resposta
        CompraCartao saved = compraCartaoRepository.save(compra);
        cartaoService.atualizarLimiteDisponivel(cartao.getId()); // Atualiza limite
        return convertCompraToResponse(saved);
    }

    private List<ParcelaCompra> gerarParcelas(CompraCartao compra, Integer diaVencimento) {
        List<ParcelaCompra> parcelas = new ArrayList<>();
        BigDecimal valorParcela = compra.getValorTotal().divide(
                BigDecimal.valueOf(compra.getQuantidadeParcelas()), 2, RoundingMode.HALF_UP
        );

        for (int i = 0; i < compra.getQuantidadeParcelas(); i++) {
            ParcelaCompra parcela = new ParcelaCompra();
            parcela.setValor(valorParcela);
            parcela.setDataVencimento(
                    calcularDataVencimento(compra.getDataCompra(), i + 1, diaVencimento)
            );
            parcela.setCompra(compra);
            parcelas.add(parcela);
        }

        return parcelas;
    }

    private LocalDate calcularDataVencimento(LocalDate dataCompra, int parcelaNumero, int diaVencimento) {
        return dataCompra.plusMonths(parcelaNumero)
                .withDayOfMonth(diaVencimento);
    }

    private void validarCategoria(Categoria categoria) {
        if (categoria.getTipo() != TipoCategoria.DESPESA) {
            throw new CategoriaInvalidaException("Categoria deve ser do tipo DESPESA");
        }
    }

    private CompraCartaoResponse convertCompraToResponse(CompraCartao compra) {

        if (compra.getCartao() == null || compra.getCategoria() == null) {
            throw new IllegalStateException("Dados incompletos na compra");
        }
        return new CompraCartaoResponse(
                compra.getId(),
                compra.getDescricao(),
                compra.getValorTotal(),
                compra.getQuantidadeParcelas(),
                compra.getDataCompra(),
                compra.getCartao().getId(),
                compra.getCartao().getNome(),
                compra.getCategoria().getId(),
                compra.getCategoria().getNome(),
                compra.getParcelas().stream()
                        .map(this::convertParcelaToResponse)
                        .toList()
        );
    }

    public List<CompraCartaoResponse> listarPorUsuario(Usuario usuario) {
        return compraCartaoRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::convertCompraToResponse)
                .collect(Collectors.toList());
    }

    // No CompraCartaoService
    public List<CompraCartaoResponse> listarPorCartao(Long cartaoId, Usuario usuario) {
        // Verifica se o cartão pertence ao usuário
        cartaoService.buscarPorIdEUsuario(cartaoId, usuario);

        // Busca compras do cartão
        List<CompraCartao> compras = compraCartaoRepository.findByCartaoId(cartaoId);

        // Converte para DTO
        return compras.stream()
                .map(this::convertCompraToResponse)
                .toList();
    }

    private ParcelaResponse convertParcelaToResponse(ParcelaCompra parcela) {
        return new ParcelaResponse(
                parcela.getId(),
                parcela.getValor(),
                parcela.getDataVencimento(),
                parcela.getStatus()
        );
    }
}