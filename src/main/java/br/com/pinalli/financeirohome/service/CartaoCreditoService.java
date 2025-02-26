package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.*;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.CompraCartao;
import br.com.pinalli.financeirohome.model.ParcelaCompra;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartaoCreditoService {

        private final CartaoCreditoRepository cartaoCreditoRepository;

        public CartaoCreditoResponse criarCartaoCredito(CartaoCreditoRequest request, Usuario usuario) {
            CartaoCredito cartao = new CartaoCredito();
            cartao.setNome(request.nome());
            cartao.setBandeiraCartao(request.bandeiraCartao());
            cartao.setNumero(request.numero());
            cartao.setLimiteTotal(request.limiteTotal());
            cartao.setLimiteDisponivel(request.limiteDisponivel());
            cartao.setDiaFechamento(request.diaFechamento());
            cartao.setDiaVencimento(request.diaVencimento());
            cartao.setTotalComprasAbertas(request.totalComprasAbertas());
            cartao.setUsuario(usuario);

            CartaoCredito saved = cartaoCreditoRepository.save(cartao);
            return convertToResponse(saved);
        }

    private CartaoCreditoResponse convertToResponse(CartaoCredito cartao) {
        return new CartaoCreditoResponse(
                cartao.getId(),
                cartao.getNome(),
                cartao.getBandeiraCartao(),
                cartao.getNumero(),
                cartao.getNumero(), cartao.getDiaFechamento(),
                cartao.getDiaVencimento(),
                cartao.getLimiteTotal(),
                cartao.getLimiteDisponivel(),
                cartao.getTotalComprasAbertas()
        );
    }

    public List<CartaoCreditoResponse> listarCartoes(Usuario usuario) {
        return cartaoCreditoRepository.findByUsuario(usuario)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CartaoCredito buscarPorIdEUsuario(Long cartaoId, Usuario usuario) {
        return cartaoCreditoRepository.findByIdAndUsuarioId(cartaoId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado ou não pertence ao usuário"));
    }


    public BigDecimal calcularLimiteDisponivel(Long cartaoId) {
        return cartaoCreditoRepository.calcularLimiteDisponivel(cartaoId);
    }

    @Transactional
    public void atualizarLimiteDisponivel(Long cartaoId) {
        BigDecimal novoLimite = calcularLimiteDisponivel(cartaoId);
        cartaoCreditoRepository.atualizarLimiteDisponivel(cartaoId, novoLimite);
    }

    @Transactional
    public void atualizarLimiteDisponivel(Long cartaoId, BigDecimal valorCompra) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        // Valida se o cartão tem limite suficiente
        if (cartao.getLimiteDisponivel().compareTo(valorCompra) < 0) {
            throw new RuntimeException("Limite insuficiente no cartão");
        }

        // Atualiza o limite disponível e o total de compras em aberto
        cartao.setLimiteDisponivel(cartao.getLimiteDisponivel().subtract(valorCompra));
        cartao.setTotalComprasAbertas(cartao.getTotalComprasAbertas().add(valorCompra));

        cartaoCreditoRepository.save(cartao);
    }

    public LimiteDisponivelResponse getLimiteDisponivel(Long cartaoId) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        return new LimiteDisponivelResponse(
                cartao.getId(),
                cartao.getNome(),
                cartao.getBandeiraCartao(),
                cartao.getLimiteTotal(),
                cartao.getLimiteDisponivel(),
                cartao.getTotalComprasAbertas()
        );
    }

    private CompraCartaoResponse convertCompraToResponse(CompraCartao compra) {
        if (compra.getCartao() == null || compra.getCategoria() == null) {
            throw new IllegalStateException("Compra não possui cartão ou categoria associada");
        }        return new CompraCartaoResponse(

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
                        .map(this::convertParcelaToResponse)
                        .toList()
        );
    }

    private ParcelaResponse convertParcelaToResponse(ParcelaCompra parcela) {
        return new ParcelaResponse(
                parcela.getId(),
                parcela.getValor(),
                parcela.getDataVencimento(),
                parcela.getStatus(),
                parcela.getNumeroParcela());
    }

    // No CartaoCreditoService
    private CartaoCreditoResponse convertCartaoToResponse(CartaoCredito cartao) {
        return new CartaoCreditoResponse(
                cartao.getId(),
                cartao.getNome(),
                cartao.getBandeiraCartao(),
                cartao.getNumero(),
                cartao.getNumero(),
                cartao.getDiaFechamento(),
                cartao.getDiaVencimento(),
                cartao.getLimiteTotal(),
                cartao.getLimiteDisponivel(),
                cartao.getTotalComprasAbertas()
        );
    }

}