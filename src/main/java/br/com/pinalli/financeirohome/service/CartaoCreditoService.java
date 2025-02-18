package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CartaoCreditoRequest;
import br.com.pinalli.financeirohome.dto.CartaoCreditoResponse;
import br.com.pinalli.financeirohome.dto.CompraCartaoResponse;
import br.com.pinalli.financeirohome.dto.ParcelaResponse;
import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.CompraCartao;
import br.com.pinalli.financeirohome.model.ParcelaCompra;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.CompraCartaoRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import br.com.pinalli.financeirohome.security.CustomUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartaoCreditoService {

    private final CartaoCreditoRepository cartaoCreditoRepository;
    private final CompraCartaoRepository compraCartaoRepository;

    @Transactional
    public CartaoCreditoResponse criarCartao(CartaoCreditoRequest request, Usuario usuario) {
        CartaoCredito cartao = new CartaoCredito();
        cartao.setNome(request.nome());
        cartao.setNumero(request.numero());
        cartao.setDiaFechamento(request.diaFechamento());
        cartao.setDiaVencimento(request.diaVencimento());
        cartao.setLimiteTotal(request.limiteTotal());
        cartao.setLimiteDisponivel(request.limiteTotal()); // Disponível inicial = limite total
        cartao.setUsuario(usuario);

        CartaoCredito saved = cartaoCreditoRepository.save(cartao);
        return convertToResponse(saved);
    }

    private CartaoCreditoResponse convertToResponse(CartaoCredito cartao) {
        return new CartaoCreditoResponse(
                cartao.getId(),
                cartao.getNome(),
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
                compra.getCartao().getNome(),
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
                cartao.getNome(),
                cartao.getNumero(),
                cartao.getDiaFechamento(),
                cartao.getDiaVencimento(),
                cartao.getLimiteTotal(),
                cartao.getLimiteDisponivel()
        );
    }



}