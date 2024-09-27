package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.Compras;
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

    private final CartaoCreditoService cartaoCreditoService;

    @Autowired
    public CartaoCreditoController(CartaoCreditoService cartaoCreditoService) {
        this.cartaoCreditoService = cartaoCreditoService;
    }

    @PostMapping
    public ResponseEntity<CartaoCreditoDTO> criarCartaoCredito(@RequestBody CartaoCreditoDTO cartaoCreditoDTO) {
        CartaoCreditoDTO novoCartao = cartaoCreditoService.criarCartaoCredito(cartaoCreditoDTO);
        return ResponseEntity.ok(novoCartao);
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
    public ResponseEntity<CartaoCreditoDTO> atualizarCartaoCredito(@PathVariable Long id, @RequestBody CartaoCreditoDTO cartaoCreditoDTO) {
        cartaoCreditoDTO.setId(id);
        CartaoCreditoDTO cartaoAtualizado = cartaoCreditoService.atualizarCartaoCredito(cartaoCreditoDTO);
        if (cartaoAtualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartaoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCartaoCredito(@PathVariable Long id) {
        cartaoCreditoService.deletarCartaoCredito(id);
        return ResponseEntity.noContent().build();
    }
}
