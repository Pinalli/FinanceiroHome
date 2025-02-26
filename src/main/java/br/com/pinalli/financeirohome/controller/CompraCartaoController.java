package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CompraCartaoRequest;
import br.com.pinalli.financeirohome.dto.CompraCartaoResponse;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.service.CompraCartaoService;
import br.com.pinalli.financeirohome.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/compras-cartao")
@RequiredArgsConstructor
public class CompraCartaoController {

    private final CompraCartaoService compraService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<CompraCartaoResponse> criarCompraCartao(
            @RequestBody @Valid CompraCartaoRequest request,
            Principal principal
    ) {
        // Log para verificar o que está sendo recebido
        System.out.println("Recebendo CompraCartaoRequest: " + request);
        System.out.println("Usuário autenticado: " + principal.getName());

        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        CompraCartaoResponse response = compraService.criarCompraCartao(request, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<CompraCartaoResponse>> listarPorUsuario(Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        List<CompraCartaoResponse> response = compraService.listarPorUsuario(usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cartao/{cartaoId}")
    public ResponseEntity<List<CompraCartaoResponse>> listarPorCartao(
            @PathVariable Long cartaoId,
            Principal principal
    ) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        List<CompraCartaoResponse> response = compraService.listarPorCartao(cartaoId, usuario);
        return ResponseEntity.ok(response);
    }
}