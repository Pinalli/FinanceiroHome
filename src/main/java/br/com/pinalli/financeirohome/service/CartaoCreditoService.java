package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import br.com.pinalli.financeirohome.security.CustomUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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
    private EntityManager entityManager;

    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;



    public CartaoCreditoService(CartaoCreditoRepository cartaoCreditoRepository, UsuarioRepository usuarioRepository) {
        this.cartaoCreditoRepository = cartaoCreditoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public CartaoCreditoDTO criarCartaoCredito(CartaoCreditoDTO cartaoCreditoDTO) {
        if (cartaoCreditoDTO == null) {
            throw new IllegalArgumentException("DTO não pode ser nulo");
        }

        try {
            // Recupera o usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long usuarioId = obterIdUsuario(authentication); // Método para recuperar o ID do usuário
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

            // Associa o usuário ao cartão de crédito
            CartaoCredito cartaoCredito = cartaoCreditoDTO.toEntity();
            cartaoCredito.setUsuario(usuario);

            // Salva no repositório
            CartaoCredito savedCartaoCredito = cartaoCreditoRepository.save(cartaoCredito);
            return CartaoCreditoDTO.fromEntity(savedCartaoCredito);
        } catch (SecurityException | IllegalStateException e) {
            throw new RuntimeException("Erro de autenticação: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar cartão de crédito: " + e.getMessage(), e);
        }
    }


    @Transactional(readOnly = true)
    public BigDecimal calcularLimiteDisponivel(Long cartaoId) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado"));

        String jpql = "SELECT SUM(c.valorTotal) FROM CompraCartao c WHERE c.cartao.id = :cartaoId";
        BigDecimal totalGasto = entityManager.createQuery(jpql, BigDecimal.class)
                .setParameter("cartaoId", cartaoId)
                .getSingleResult();

        return cartao.getLimiteTotal().subtract(totalGasto != null ? totalGasto : BigDecimal.ZERO);
    }


    @Transactional
    public CartaoCredito findById(Long id) {
        return cartaoCreditoRepository.findById(id).orElseThrow(() -> new CartaoCreditoException("Cartão de crédito não encontrado."));
    }

    @Transactional
    Long obterIdUsuario(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        } else if (principal instanceof Usuario) {
            return ((Usuario) principal).getId();
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) principal;
            String username = springUser.getUsername();
            return usuarioRepository.findByEmail(username)
                    .map(Usuario::getId)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o username: " + username));
        } else if (principal instanceof String) {
            String email = (String) principal;
            return usuarioRepository.findByEmail(email)
                    .map(Usuario::getId)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
        } else {
            throw new IllegalStateException("Tipo de usuário não suportado: " + principal.getClass().getName());
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
        cartaoExistente.setBandeiraCartao(cartaoCreditoDTO.getBandeiraCartao());
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
                .bandeiraCartao(cartaoCredito.getBandeiraCartao())
                .limite(cartaoCredito.getLimite())
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
        atualizarLimiteEComprasAbertas(cartaoId, valorCompra, true);
    }

    @Transactional
    public void atualizarCompra(Long cartaoId, BigDecimal diferencaValor) {
        atualizarLimiteEComprasAbertas(cartaoId, diferencaValor, false);
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

    @Transactional
    public void atualizarLimiteEComprasAbertas(Long cartaoId, BigDecimal valor, boolean isNovaCompra) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                .orElseThrow(() -> new CartaoCreditoException("Cartão de crédito não encontrado"));

        if (isNovaCompra) {
            cartao.setLimiteDisponivel(cartao.getLimiteDisponivel().subtract(valor));
            cartao.setTotalComprasAbertas(cartao.getTotalComprasAbertas().add(valor));
        } else {
            // Pagamento de parcela
            cartao.setLimiteDisponivel(cartao.getLimiteDisponivel().add(valor));
            cartao.setTotalComprasAbertas(cartao.getTotalComprasAbertas().subtract(valor));
        }

        cartaoCreditoRepository.save(cartao);
    }

    public CartaoCreditoDTO obterLimiteEComprasAbertas(Long cartaoId) {
        return cartaoCreditoRepository.findById(cartaoId)
                .map(cartao -> new CartaoCreditoDTO(
                        cartao.getId(),
                        cartao.getBandeiraCartao(),
                        cartao.getLimite(),
                        cartao.getLimiteDisponivel(),
                        cartao.getTotalComprasAbertas()
                ))
                .orElseThrow(() -> new CartaoCreditoException("Cartão de crédito não encontrado"));
    }
}
