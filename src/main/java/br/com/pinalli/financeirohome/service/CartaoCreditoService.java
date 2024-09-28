package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import br.com.pinalli.financeirohome.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;


@Service
public class CartaoCreditoService {

    private final UsuarioRepository usuarioRepository;
    private final CartaoCreditoRepository cartaoCreditoRepository;


    public CartaoCreditoService(CartaoCreditoRepository cartaoCreditoRepository, UsuarioRepository usuarioRepository) {
        this.cartaoCreditoRepository = cartaoCreditoRepository;
        this.usuarioRepository = usuarioRepository;
    }


    public CartaoCredito criarCartaoCredito(CartaoCredito cartaoCredito) {
        if (cartaoCredito == null || cartaoCredito.getUsuario() == null || cartaoCredito.getUsuario().getId() == null) {
            throw new IllegalArgumentException("Dados inválidos para criar o cartão de crédito.");
        }

        return cartaoCreditoRepository.save(cartaoCredito);
    }
    public Long obterIdUsuario(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        } else if (principal instanceof String) {
            String email = (String) principal;
            Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
            return usuario.getId();
        } else {
            throw new IllegalStateException("Tipo de usuário não suportado.");
        }
    }

    public List<CartaoCreditoDTO> buscarCartoesPorUsuario(Long usuarioId) {
        return cartaoCreditoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(CartaoCreditoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public CartaoCreditoDTO atualizarCartaoCredito(CartaoCreditoDTO cartaoCreditoDTO, Long idUsuario) {
        CartaoCredito cartaoExistente = cartaoCreditoRepository.findById(cartaoCreditoDTO.getId())
                .orElseThrow(() -> new RuntimeException("Cartão de crédito não encontrado"));

        if (!cartaoExistente.getUsuario().getId().equals(idUsuario)) {
            throw new SecurityException("Usuário não autorizado a atualizar este cartão de crédito");
        }

        CartaoCredito cartaoAtualizado = converterDtoParaEntidade(cartaoCreditoDTO);
        cartaoAtualizado.setUsuario(cartaoExistente.getUsuario());

        CartaoCredito savedCartao = cartaoCreditoRepository.save(cartaoAtualizado);
        return converterParaDTO(savedCartao);
    }

    public Usuario obterUsuarioPorId(Long id){
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public CartaoCreditoDTO converterParaDTO(CartaoCredito cartaoCredito) {
        return CartaoCreditoDTO.fromEntity(cartaoCredito);
    }

    public CartaoCredito converterDtoParaEntidade(CartaoCreditoDTO cartaoCreditoDTO){
        if(cartaoCreditoDTO == null) throw new IllegalArgumentException("DTO não pode ser nulo");


        Usuario usuario = usuarioRepository.findById(cartaoCreditoDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        return CartaoCredito.builder()
                .descricao(cartaoCreditoDTO.getDescricao())
                .limite(cartaoCreditoDTO.getLimite())
                .valor(cartaoCreditoDTO.getValor())
                .usuario(usuario) // Importante: usuário correto
                .build();
    }


    public CartaoCreditoDTO buscarCartaoCreditoPorId(Long id) {
        return cartaoCreditoRepository.findById(id)
                .map(CartaoCreditoDTO::fromEntity)
                .orElse(null);
    }

    public CartaoCreditoDTO atualizarCartaoCredito(CartaoCreditoDTO cartaoCreditoDTO) {
        if (cartaoCreditoRepository.existsById(cartaoCreditoDTO.getId())) {
            CartaoCredito cartaoCredito = cartaoCreditoDTO.toEntity();
            CartaoCredito updatedCartaoCredito = cartaoCreditoRepository.save(cartaoCredito);
            return CartaoCreditoDTO.fromEntity(updatedCartaoCredito);
        }
        return null;
    }

    public void deletarCartaoCredito(Long id) {
        cartaoCreditoRepository.deleteById(id);
    }
}