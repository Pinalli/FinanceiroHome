package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.ComprasRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import br.com.pinalli.financeirohome.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import java.util.stream.Collectors;


@Service
public class CartaoCreditoService {

    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComprasRepository comprasRepository;

    public CartaoCreditoService(CartaoCreditoRepository cartaoCreditoRepository, UsuarioRepository usuarioRepository) {
        this.cartaoCreditoRepository = cartaoCreditoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public CartaoCreditoDTO criarCartaoCredito(CartaoCreditoDTO cartaoCreditoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (cartaoCreditoDTO == null) {
            throw new IllegalArgumentException("DTO não pode ser nulo");
        }

        Long usuarioId = obterIdUsuario(authentication);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

        CartaoCredito cartaoCredito = cartaoCreditoDTO.toEntity();
        cartaoCredito.setUsuario(usuario);

        CartaoCredito savedCartaoCredito = cartaoCreditoRepository.save(cartaoCredito);
        return CartaoCreditoDTO.fromEntity(savedCartaoCredito);
    }


    public CartaoCreditoDTO findById(Long id) {
        CartaoCredito cartaoCredito = cartaoCreditoRepository.findById(id).orElseThrow();
        return CartaoCreditoDTO.fromEntity(cartaoCredito);
    }

    @Transactional
    Long obterIdUsuario(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }

        Object principal = authentication.getPrincipal();

        // Verificando o tipo de objeto, importante para segurança.
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        } else if (principal instanceof String) {
            String email = (String) principal;
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
            return usuario.getId();
        } else {
            throw new IllegalStateException("Tipo de usuário não suportado.");
        }
    }

    @Transactional
    public List<CartaoCreditoDTO> buscarCartoesPorUsuario(Long usuarioId) {
        return cartaoCreditoRepository.findByUsuarioId(usuarioId).stream()
                .map(CartaoCreditoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartaoCreditoDTO atualizarCartaoCredito(CartaoCreditoDTO cartaoCreditoDTO) {
        if (cartaoCreditoDTO == null || cartaoCreditoDTO.getId() == null) {
            throw new IllegalArgumentException("Dados inválidos para atualizar o cartão de crédito.");
        }

        CartaoCredito cartaoExistente = cartaoCreditoRepository.findById(cartaoCreditoDTO.getId())
                .orElseThrow(() -> new CartaoCreditoException("Cartão de crédito não encontrado."));

        // Atualiza os campos
        cartaoExistente.setDescricao(cartaoCreditoDTO.getDescricao());
        cartaoExistente.setLimite(cartaoCreditoDTO.getLimite());
        cartaoExistente.setValor(cartaoCreditoDTO.getValor());

        CartaoCredito cartaoAtualizado = cartaoCreditoRepository.save(cartaoExistente);
        return CartaoCreditoDTO.fromEntity(cartaoAtualizado);
    }

    public Usuario obterUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public CartaoCreditoDTO converterParaDTO(CartaoCredito cartaoCredito) {
        if (cartaoCredito == null) {
            return null;
        }
        return CartaoCreditoDTO.builder()
                .id(cartaoCredito.getId())
                .descricao(cartaoCredito.getDescricao())
                .limite(cartaoCredito.getLimite())
                .build();
    }

    @Transactional
    public CartaoCredito converterDtoParaEntidade(CartaoCreditoDTO cartaoCreditoDTO) {
        if (cartaoCreditoDTO == null) throw new IllegalArgumentException("DTO não pode ser nulo");

        Usuario usuario;
        try {
            usuario = usuarioRepository.findById(cartaoCreditoDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + cartaoCreditoDTO.getUsuarioId()));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage());
        }

        return CartaoCredito.builder()
                .descricao(cartaoCreditoDTO.getDescricao())
                .limite(cartaoCreditoDTO.getLimite())
                .valor(cartaoCreditoDTO.getValor())
                .usuario(usuario)
                .build();
    }

    @Transactional
    public CartaoCreditoDTO buscarCartaoCreditoPorId(Long id) {
        return cartaoCreditoRepository.findById(id)
                .map(CartaoCreditoDTO::fromEntity)
                .orElse(null);
    }

    @Transactional
    public void deletarCartaoCredito(Long id) {
        cartaoCreditoRepository.deleteById(id);
    }

    @Transactional
    public void registrarCompra(Long cartaoId, BigDecimal valorCompra) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                .orElseThrow(() -> new CartaoCreditoException("Cartão de crédito não encontrado."));

        cartao.setLimiteDisponivel(cartao.getLimiteDisponivel().subtract(valorCompra));
        cartao.setTotalComprasAbertas(cartao.getTotalComprasAbertas().add(valorCompra));

        cartaoCreditoRepository.save(cartao);
    }

    @Transactional
    public void atualizarCompra(Long cartaoId, BigDecimal diferencaValor) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                .orElseThrow(() -> new CartaoCreditoException("Cartão de crédito não encontrado."));

        cartao.setLimiteDisponivel(cartao.getLimiteDisponivel().subtract(diferencaValor));
        cartao.setTotalComprasAbertas(cartao.getTotalComprasAbertas().add(diferencaValor));

        cartaoCreditoRepository.save(cartao);
    }

    @Transactional
    public void cancelarCompra(Long cartaoId, BigDecimal valorCompra) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                .orElseThrow(() -> new CartaoCreditoException("Cartão de crédito não encontrado."));

        cartao.setLimiteDisponivel(cartao.getLimiteDisponivel().add(valorCompra));
        cartao.setTotalComprasAbertas(cartao.getTotalComprasAbertas().subtract(valorCompra));

        cartaoCreditoRepository.save(cartao);
    }

    @Transactional
    public BigDecimal getLimiteDisponivel(Long cardId) {
        return cartaoCreditoRepository.findById(cardId)
                .map(CartaoCredito::getLimiteDisponivel)
                .orElseThrow(() -> new CartaoCreditoException("Cartão de crédito não encontrado."));
    }

    @Transactional
    public BigDecimal getTotalComprasAbertas(Long cardId) {
        return cartaoCreditoRepository.findById(cardId)
                .map(CartaoCredito::getTotalComprasAbertas)
                .orElseThrow(() -> new CartaoCreditoException("Cartão de crédito não encontrado."));
    }


    @Transactional
    public List<CartaoCredito> getCartaoCreditoByUsuarioId(Long usuarioId) {
        return cartaoCreditoRepository.findByUsuarioId(usuarioId); // Assume que findByUsuarioId retorna uma List<CartaoCredito>
    }


}