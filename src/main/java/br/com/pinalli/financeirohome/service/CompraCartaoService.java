package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CompraCartaoRequest;
import br.com.pinalli.financeirohome.dto.CompraCartaoResponse;

import br.com.pinalli.financeirohome.dto.ParcelaResponse;
import br.com.pinalli.financeirohome.exception.CategoriaInvalidaException;
import br.com.pinalli.financeirohome.model.*;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.repository.CategoriaRepository;
import br.com.pinalli.financeirohome.repository.CompraCartaoRepository;
import br.com.pinalli.financeirohome.repository.ParcelaCompraRepository;
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
    private final CartaoCreditoRepository cartaoCreditoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ParcelaCompraRepository parcelaCompraRepository;
    private final CartaoCreditoService cartaoCreditoService;

    public CompraCartaoResponse criarCompraCartao(CompraCartaoRequest request, Usuario usuario) {
        // Busca o cartão e a categoria
        CartaoCredito cartao = cartaoCreditoRepository.findById(request.cartaoId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Cria a compra
        CompraCartao compra = new CompraCartao();
        compra.setDescricao(request.descricao());
        compra.setValorTotal(request.valorTotal());
        compra.setQuantidadeParcelas(request.quantidadeParcelas());
        compra.setDataCompra(request.dataCompra());
        compra.setCartao(cartao);
        compra.setCategoria(categoria);
        compra.setUsuario(usuario);
        // Salva a compra primeiro, garantindo que tenha um ID
        CompraCartao compraSalva = compraCartaoRepository.save(compra);

        // Agora que compraSalva tem um ID, criamos as parcelas
        List<ParcelaCompra> parcelas = criarParcelas(compraSalva);
        parcelas.forEach(parcela -> parcela.setCompra(compraSalva)); // Garante a associação correta

        // Salva todas as parcelas
        parcelaCompraRepository.saveAll(parcelas);

        // Atualiza a compra com as parcelas
        compraSalva.setParcelas(parcelas);
        compraCartaoRepository.save(compraSalva); // Atualiza a compra com as parcelas associadas

        // Após salvar a compra, atualiza o limite disponível do cartão
        cartaoCreditoService.atualizarLimiteDisponivel(request.cartaoId(), request.valorTotal());

        // Retorna a resposta
        return CompraCartaoResponse.fromCompraCartao(compraSalva);
    }

    private List<ParcelaCompra> criarParcelas(CompraCartao compra) {
        List<ParcelaCompra> parcelas = new ArrayList<>();
        BigDecimal valorParcela = compra.getValorTotal().divide(
                BigDecimal.valueOf(compra.getQuantidadeParcelas()), 2, RoundingMode.HALF_UP
        );

        for (int i = 1; i <= compra.getQuantidadeParcelas(); i++) {
            ParcelaCompra parcela = new ParcelaCompra();
            parcela.setValor(valorParcela);
            parcela.setDataVencimento(compra.getDataCompra().plusMonths(i));
            parcela.setStatus(StatusParcelaCompra.PENDENTE); // Define o status como "PENDENTE"
            parcela.setNumeroParcela(i); // Define o número da parcela (1, 2, 3, etc.)
            parcela.setCompra(compra); // Associa a parcela à compra
            parcelas.add(parcela);
        }

        // Salva as parcelas no banco de dados
        return parcelaCompraRepository.saveAll(parcelas);
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
        // Verifica se os dados necessários estão presentes
        if (compra.getCartao() == null || compra.getCategoria() == null) {
            throw new IllegalStateException("Dados incompletos na compra");
        }

        // Converte a compra para o DTO de resposta
        return new CompraCartaoResponse(
                compra.getId(),
                compra.getDescricao(),
                compra.getValorTotal(),
                compra.getQuantidadeParcelas(),
                compra.getDataCompra(),
                compra.getCartao().getId(),
                compra.getCartao().getBandeiraCartao(),
                compra.getCategoria().getId(),
                compra.getCategoria().getNome(),
                compra.getParcelas().stream()
                        .map(this::convertParcelaToResponse) // Converte cada parcela para DTO
                        .toList() // Converte para lista
        );
    }

    private ParcelaResponse convertParcelaToResponse(ParcelaCompra parcela) {
        return new ParcelaResponse(
                parcela.getId(), // ID da parcela
                parcela.getValor(), // Valor da parcela
                parcela.getDataVencimento(), // Data de vencimento
                parcela.getStatus(), // Status da parcela
                parcela.getNumeroParcela());
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


}