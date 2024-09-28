package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.exception.UsuarioNaoAutenticadoException;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.service.CartaoCreditoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/cartoes-credito")
public class CartaoCreditoController {

    private final CartaoCreditoService cartaoCreditoService;

    public CartaoCreditoController(CartaoCreditoService cartaoCreditoService) {
        this.cartaoCreditoService = cartaoCreditoService;
    }

    @PostMapping
    public ResponseEntity<CartaoCreditoDTO> criarCartaoCredito(@Valid @RequestBody CartaoCreditoDTO cartaoCreditoDTO, Authentication authentication) {
        try {
            verificarAutenticacao(authentication);

            Long idUsuario = cartaoCreditoService.obterIdUsuario(authentication);
            if (idUsuario == null) {
                throw new UsuarioNaoAutenticadoException("Falha na obtenção do ID do usuário");
            }

            CartaoCredito cartao = cartaoCreditoService.converterDtoParaEntidade(cartaoCreditoDTO);
            cartao.setUsuario(cartaoCreditoService.obterUsuarioPorId(idUsuario));

            CartaoCredito novoCartao = cartaoCreditoService.criarCartaoCredito(cartao);
            return new ResponseEntity<>(cartaoCreditoService.converterParaDTO(novoCartao), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dados inválidos fornecidos: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o cartão de crédito");
        }
    }

    private void verificarAutenticacao(Authentication authentication) {
        Objects.requireNonNull(authentication, "Authentication não pode ser nula.");
        if (!authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CartaoCreditoDTO>> buscarCartoesPorUsuario(@PathVariable Long usuarioId) {
        List<CartaoCreditoDTO> cartoes = cartaoCreditoService.buscarCartoesPorUsuario(usuarioId);
        return ResponseEntity.ok(cartoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartaoCreditoDTO> buscarCartaoCredito(@PathVariable Long id) {
        CartaoCreditoDTO cartao = cartaoCreditoService.buscarCartaoCreditoPorId(id);
        if (cartao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaoCreditoDTO> atualizarCartaoCredito(@PathVariable Long id, @Valid @RequestBody CartaoCreditoDTO cartaoCreditoDTO, Authentication authentication) {
        try {
            verificarAutenticacao(authentication);

            Long idUsuario = cartaoCreditoService.obterIdUsuario(authentication);
            if (idUsuario == null) {
                throw new SecurityException("Falha na obtenção do ID do usuário");
            }

            cartaoCreditoDTO.setId(id);
            CartaoCreditoDTO cartaoAtualizado = cartaoCreditoService.atualizarCartaoCredito(cartaoCreditoDTO, idUsuario);
            if (cartaoAtualizado == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cartaoAtualizado);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCartaoCredito(@PathVariable Long id) {
        cartaoCreditoService.deletarCartaoCredito(id);
        return ResponseEntity.noContent().build();
    }
}
