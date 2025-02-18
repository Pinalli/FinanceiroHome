package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CartaoCreditoRequest;
import br.com.pinalli.financeirohome.dto.CartaoCreditoResponse;
import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.CompraCartaoRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import br.com.pinalli.financeirohome.security.CustomUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    public BigDecimal calcularLimiteDisponivel(Long cartaoId) {
        return cartaoCreditoRepository.calcularLimiteDisponivel(cartaoId);
    }

    @Transactional
    public void atualizarLimiteDisponivel(Long cartaoId) {
        BigDecimal novoLimite = calcularLimiteDisponivel(cartaoId);
        cartaoCreditoRepository.atualizarLimiteDisponivel(cartaoId, novoLimite);
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
}