package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CartaoCreditoRequest;
import br.com.pinalli.financeirohome.dto.CartaoCreditoResponse;
import br.com.pinalli.financeirohome.model.LimiteCartaoView;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.LimiteCartaoViewRepository;
import br.com.pinalli.financeirohome.service.CartaoCreditoService;
import br.com.pinalli.financeirohome.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cartao_credito")
@RequiredArgsConstructor
public class CartaoCreditoController {

    private final CartaoCreditoService cartaoCreditoService;
    private final UsuarioService usuarioService;
    private final LimiteCartaoViewRepository limiteCartaoViewRepository;


    @PostMapping
    public ResponseEntity<CartaoCreditoResponse> criarCartao(
            @RequestBody @Valid CartaoCreditoRequest request,
            Principal principal
    ) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        CartaoCreditoResponse response = cartaoCreditoService.criarCartao(request, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/limite-disponivel")
    public ResponseEntity<BigDecimal> getLimiteDisponivel(@PathVariable Long id) {
        return ResponseEntity.ok(cartaoCreditoService.calcularLimiteDisponivel(id));
    }

    @GetMapping("/{id}/limite")
    public ResponseEntity<Map<String, BigDecimal>> getLimite(@PathVariable Long id) {
        LimiteCartaoView view = limiteCartaoViewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado"));
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("limiteTotal", view.getLimiteTotal());
        response.put("limiteDisponivel", view.getLimiteDisponivel());

        return ResponseEntity.ok(response);
    }
}