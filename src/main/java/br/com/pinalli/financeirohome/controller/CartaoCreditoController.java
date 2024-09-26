package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.Compra;
import br.com.pinalli.financeirohome.service.CartaoCreditoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cartoes-credito")
public class CartaoCreditoController {
    @Autowired
    private CartaoCreditoService cartaoCreditoService;

    public CartaoCreditoController(CartaoCreditoService cartaoCreditoService) {
        this.cartaoCreditoService = cartaoCreditoService;
    }

    @PostMapping
    public ResponseEntity<CartaoCredito> criarCartaoCredito(@RequestBody CartaoCredito cartaoCredito) {
        CartaoCredito novoCartao = cartaoCreditoService.criarCartaoCredito(cartaoCredito);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCartao);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CartaoCredito>> listarCartoesPorUsuario(@PathVariable Long usuarioId) {
        List<CartaoCredito> cartoes = cartaoCreditoService.listarCartoesPorUsuario(usuarioId);
        return ResponseEntity.ok(cartoes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaoCredito> atualizarCartaoCredito(@PathVariable Long id, @RequestBody CartaoCredito cartaoCredito) {
        cartaoCredito.setId(id);
        CartaoCredito cartaoAtualizado = cartaoCreditoService.atualizarCartaoCredito(cartaoCredito);
        return ResponseEntity.ok(cartaoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCartaoCredito(@PathVariable Long id) {
        cartaoCreditoService.deletarCartaoCredito(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/limite-disponivel")
    public ResponseEntity<BigDecimal> getLimiteDisponivel(@PathVariable Long id) {
        BigDecimal limiteDisponivel = cartaoCreditoService.calcularLimiteDisponivel(id);
        return ResponseEntity.ok(limiteDisponivel);
    }

    @PostMapping("/{cartaoId}/compras")
    public ResponseEntity<Compra> registrarCompra(@PathVariable Long cartaoId, @RequestBody Compra compra) {
        compra.setCartaoCredito(new CartaoCredito());
        compra.getCartaoCredito().setId(cartaoId);
        Compra novaCompra = cartaoCreditoService.registrarCompra(compra);
        return ResponseEntity.ok(novaCompra);
    }

    @GetMapping("/{cartaoId}/compras")
    public ResponseEntity<List<Compra>> listarComprasPorCartao(@PathVariable Long cartaoId) {
        List<Compra> compras = cartaoCreditoService.listarComprasPorCartao(cartaoId);
        return ResponseEntity.ok(compras);
    }

    @PostMapping("/compras/{compraId}/pagar-parcela")
    public ResponseEntity<Void> pagarParcela(@PathVariable Long compraId) {
        cartaoCreditoService.pagarParcela(compraId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{cartaoId}/total-compras-em-aberto")
    public ResponseEntity<BigDecimal> getTotalComprasEmAberto(@PathVariable Long cartaoId) {
        BigDecimal total = cartaoCreditoService.calcularTotalComprasEmAberto(cartaoId);
        return ResponseEntity.ok(total);
    }
}