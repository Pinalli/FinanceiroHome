package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ComprasDTO;

import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.exception.CompraNotFoundException;
import br.com.pinalli.financeirohome.exception.CompraValidationException;

import br.com.pinalli.financeirohome.model.Compras;

import br.com.pinalli.financeirohome.service.ComprasService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {

    private final ComprasService comprasService;

    @Autowired
    public ComprasController(ComprasService comprasService) {
        this.comprasService = comprasService;
    }

    @PostMapping
    public ResponseEntity<ComprasDTO> registrarCompra(@PathVariable Long cartaoId, @RequestBody ComprasDTO comprasDTO) {
        ComprasDTO novaCompra = comprasService.registrarCompra(comprasDTO, cartaoId);
        return ResponseEntity.ok(novaCompra);
    }

    @GetMapping
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
