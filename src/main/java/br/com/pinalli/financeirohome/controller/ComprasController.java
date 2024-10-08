package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ComprasDTO;
import br.com.pinalli.financeirohome.exception.CompraNotFoundException;
import br.com.pinalli.financeirohome.exception.CompraValidationException;
import br.com.pinalli.financeirohome.service.CartaoCreditoService;
import br.com.pinalli.financeirohome.service.ComprasService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {


    private static final Logger log = LoggerFactory.getLogger(ComprasService.class); // For SLF4j

    private final ComprasService comprasService;
    @Autowired
    private CartaoCreditoService cartaoCreditoService;

    @Autowired
    public ComprasController(ComprasService comprasService, CartaoCreditoService cartaoCreditoService) {
        this.comprasService = comprasService;
        this.cartaoCreditoService = cartaoCreditoService;
    }


    @PostMapping("/{cartaoId}/compras")
    public ResponseEntity<ComprasDTO> registrarCompra(@PathVariable Long cartaoId, @Valid @RequestBody ComprasDTO comprasDTO, Authentication authentication) {

        try {
            if (comprasDTO.getCartaoCredito() == null || comprasDTO.getCartaoCredito().getId() == null) {
                log.error("Cartão de crédito não fornecido ou ID é nulo.");
                throw new IllegalArgumentException("Cartão de crédito não pode ser nulo.");
            }
            log.debug("ComprasDTO recebido no controller: {}", comprasDTO);
            ComprasDTO novaCompra = comprasService.registrarCompra(comprasDTO, cartaoId, authentication);
            return ResponseEntity.ok(novaCompra);

        } catch (SecurityException e) {
            log.error("Erro de segurança ao registrar compra", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401

        } catch (IllegalArgumentException e) {
            log.error("Erro nos dados de entrada: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);  // 400

        } catch (Exception e) {
            log.error("Erro inesperado ao registrar compra", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // 500
        }
    }

    @GetMapping("/{cartaoId}/compras")
    public ResponseEntity<List<ComprasDTO>> listarComprasPorCartao(@PathVariable Long cartaoId, Authentication authentication) {
        try {
            log.debug("Iniciando listarComprasPorCartao para cartaoId: {}", cartaoId);
            List<ComprasDTO> compras = comprasService.listarComprasPorCartao(cartaoId, authentication);
            log.debug("Compras recuperadas: {}", compras);
            return ResponseEntity.ok(compras);
        } catch (Exception e) {
            log.error("Erro ao listar compras para o cartão {}: ", cartaoId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private void verificarAutenticacao(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }
    }

    @GetMapping("/{cartaoId}/compra/{compraId}")
    public ResponseEntity<ComprasDTO> buscarCompra(@PathVariable Long cartaoId, @PathVariable Long compraId) {
        if (cartaoId == null || cartaoId <= 0 || compraId == null || compraId <= 0) {
            throw new CompraValidationException("ID do cartão ou da compra inválido.");
        }
        ComprasDTO compra = comprasService.buscarCompraPorId(compraId);
        if (compra == null || !compra.getCartaoCredito().getId().equals(cartaoId)) {
            throw new CompraNotFoundException(compraId);
        }
        return ResponseEntity.ok(compra);
    }

    @PutMapping("/{compraId}")
    public ResponseEntity<ComprasDTO> atualizarCompra(
            @PathVariable Long compraId,
            @Valid @RequestBody ComprasDTO compraAtualizada,
            Authentication authentication) {

        try {
            verificarAutenticacao(authentication);
            compraAtualizada.setId(compraId);
            ComprasDTO compraDTO = comprasService.atualizarCompra(compraAtualizada, authentication);
            if(compraDTO == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(compraDTO);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("/{compraId}")
    public ResponseEntity<Void> deletarCompra(@PathVariable Long compraId, Authentication authentication) {
        try {
            verificarAutenticacao(authentication);
            if (comprasService.deletarCompra(compraId, authentication)) {
                return ResponseEntity.noContent().build(); // Status 204
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //Tratamento adequado de erros
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}