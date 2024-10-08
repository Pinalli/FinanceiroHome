package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.service.CartaoCreditoService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cartoes-credito")
public class CartaoCreditoController {

    private final CartaoCreditoService cartaoCreditoService;

    public CartaoCreditoController(CartaoCreditoService cartaoCreditoService) {
        this.cartaoCreditoService = cartaoCreditoService;
    }

    @PostMapping
    public ResponseEntity<?> criarCartaoCredito(@Valid @RequestBody CartaoCreditoDTO cartaoCreditoDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            CartaoCreditoDTO novoCartao = cartaoCreditoService.criarCartaoCredito(cartaoCreditoDTO);
            return new ResponseEntity<>(novoCartao, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar cartão de crédito: " + e.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
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
    public ResponseEntity<CartaoCreditoDTO> atualizarCartaoCredito(@PathVariable Long id, @Valid @RequestBody CartaoCreditoDTO cartaoCreditoDTO) {
        try {
            //Importantíssimo: seta o id do DTO para a requisição
            cartaoCreditoDTO.setId(id);
            CartaoCreditoDTO atualizado = cartaoCreditoService.atualizarCartaoCredito(cartaoCreditoDTO);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (CartaoCreditoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Correção
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorno correto para exceções mais gerais
        }
    }

    @GetMapping("/{idCartaoCredito}/limite")
    public ResponseEntity<CartaoCredito> getLimiteCartaoCredito(@PathVariable Long idCartaoCredito) {
        try {
            CartaoCredito cartaoCredito = cartaoCreditoService.getLimiteCartaoCredito(idCartaoCredito);
            return ResponseEntity.ok(cartaoCredito);
        } catch (CartaoCreditoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCartaoCredito(@PathVariable Long id) {
        cartaoCreditoService.deletarCartaoCredito(id);
        return ResponseEntity.noContent().build();
    }


}
