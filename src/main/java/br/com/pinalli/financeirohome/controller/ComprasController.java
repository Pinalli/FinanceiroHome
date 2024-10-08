package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CompraCreateDTO;
import br.com.pinalli.financeirohome.dto.ComprasDTO;
import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.exception.CompraNotFoundException;
import br.com.pinalli.financeirohome.exception.CompraValidationException;
import br.com.pinalli.financeirohome.service.CartaoCreditoService;
import br.com.pinalli.financeirohome.service.ComprasService;
import jakarta.transaction.Transactional;
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
    public ComprasController(ComprasService comprasService,  CartaoCreditoService cartaoCreditoService) {
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

  /*  @GetMapping
    public ResponseEntity<List<ComprasDTO>> listarCompras(@PathVariable Long cartaoId) {
        if (cartaoId == null || cartaoId <= 0) {
            throw new CompraValidationException("ID do cartão de crédito inválido.");
        }
        List<ComprasDTO> compras = comprasService.listarComprasPorCartao(cartaoId);
        if (compras.isEmpty()) {
            throw new CartaoCreditoException(cartaoId);
        }
        return ResponseEntity.ok(compras);
    }
*/
    @GetMapping("/cartao/{cartaoId}")
    public ResponseEntity<List<ComprasDTO>> listarComprasPorCartao(@PathVariable Long cartaoId, Authentication authentication) {
        try {
            verificarAutenticacao(authentication); //Verifica autenticação.
            List<ComprasDTO> compras = comprasService.listarComprasPorCartao(cartaoId, authentication);
            if (compras == null || compras.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(compras);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private void verificarAutenticacao(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }
    }

    @GetMapping("/{compraId}")
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
    public ResponseEntity<ComprasDTO> atualizarCompra(@PathVariable Long cartaoId,
                                                      @PathVariable Long compraId, @RequestBody ComprasDTO compraAtualizada) {
        if (cartaoId == null || cartaoId <= 0 || compraId == null || compraId <= 0) {
            throw new CompraValidationException("ID do cartão ou da compra inválido.");
        }
        compraAtualizada.setId(compraId);
        ComprasDTO compra = comprasService.atualizarCompra(compraAtualizada);
        if (compra == null || !compra.getCartaoCredito().getId().equals(cartaoId)) {
            throw new CompraNotFoundException(compraId);
        }
        return ResponseEntity.ok(compra);
    }

    @DeleteMapping("/{compraId}")
    public ResponseEntity<Void> deletarCompra(@PathVariable Long cartaoId, @PathVariable Long compraId) {
        if (cartaoId == null || cartaoId <= 0 || compraId == null || compraId <= 0) {
            throw new CompraValidationException("ID do cartão ou da compra inválido.");
        }
        comprasService.deletarCompra(compraId);
        return ResponseEntity.noContent().build();
    }
}
