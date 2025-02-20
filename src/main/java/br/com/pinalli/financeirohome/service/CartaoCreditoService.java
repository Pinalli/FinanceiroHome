package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CartaoCreditoRequest;
import br.com.pinalli.financeirohome.dto.CartaoCreditoResponse;
import br.com.pinalli.financeirohome.dto.CompraCartaoResponse;
import br.com.pinalli.financeirohome.dto.ParcelaResponse;
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

@Service
@RequiredArgsConstructor
public class CartaoCreditoService {

    private final CartaoCreditoRepository cartaoCreditoRepository;

    public CartaoCreditoResponse criarCartao(CartaoCreditoRequest request, Usuario usuario) {
        CartaoCredito cartao = new CartaoCredito();
        cartao.setBandeiraCartao(request.bandeiraCartao());
        cartao.setNumero(request.numero());
        cartao.setLimiteTotal(request.limite());
        cartao.setLimiteDisponivel(request.limiteDisponivel());
        cartao.setDiaFechamento(request.diaFechamento());
        cartao.setDiaVencimento(request.diaVencimento());
        cartao.setUsuario(usuario);

        CartaoCredito saved = cartaoCreditoRepository.save(cartao);
        return convertToResponse(saved);
    }

    private CartaoCreditoResponse convertToResponse(CartaoCredito cartao) {
        return new CartaoCreditoResponse(
                cartao.getId(),
                cartao.getBandeiraCartao(),
                cartao.getNumero(),
                cartao.getDiaFechamento(),
                cartao.getDiaVencimento(),
                cartao.getLimiteTotal(),
                cartao.getLimiteDisponivel()
        );
    }

    public List<CartaoCreditoResponse> listarCartoes(Usuario usuario) {
        return cartaoCreditoRepository.findByUsuario(usuario)
                .stream()
                .map(this::convertToResponse)
                .toList();
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
                parcela.getStatus()
        );
    }

    // No CartaoCreditoService
    private CartaoCreditoResponse convertCartaoToResponse(CartaoCredito cartao) {
        return new CartaoCreditoResponse(
                cartao.getId(),
                cartao.getBandeiraCartao(),
                cartao.getNumero(),
                cartao.getDiaFechamento(),
                cartao.getDiaVencimento(),
                cartao.getLimiteTotal(),
                cartao.getLimiteDisponivel()
        );
    }
}