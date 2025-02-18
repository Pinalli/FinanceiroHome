package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ContaRequest;
import br.com.pinalli.financeirohome.dto.ContaResponse;
import br.com.pinalli.financeirohome.model.TipoConta;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.service.ContaService;
import br.com.pinalli.financeirohome.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;
    private final UsuarioService usuarioService;

    // Endpoint para criar conta (apenas usuário autenticado)
    @PostMapping
    public ResponseEntity<ContaResponse> criarConta(
            @RequestBody @Valid ContaRequest request,
            Principal principal
    ) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        ContaResponse response = contaService.criarConta(request, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint único para listar contas por tipo (usando DTO e segurança)
    @GetMapping("/{tipo}")
    public ResponseEntity<List<ContaResponse>> listarContasPorTipo(
            @PathVariable String tipo,
            Principal principal
    ) {
        try {
            TipoConta tipoConta = TipoConta.valueOf(tipo.toUpperCase());
            Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
            List<ContaResponse> contas = contaService.listarContasPorTipo(tipoConta, usuario);
            return ResponseEntity.ok(contas);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de conta inválido: " + tipo);
        }
    }
}