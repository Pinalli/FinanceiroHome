package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ContaDTO;
import br.com.pinalli.financeirohome.model.Conta;
import br.com.pinalli.financeirohome.model.TipoConta;
import br.com.pinalli.financeirohome.repository.ContaRepository;
import br.com.pinalli.financeirohome.service.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaService contaService;
    private final ContaRepository contaRepository;

    public ContaController(ContaService contaService, ContaRepository contaRepository) {
        this.contaService = contaService;
        this.contaRepository = contaRepository;
    }

    @PostMapping
    public ResponseEntity<ContaDTO> criarConta(@RequestBody ContaDTO contaDTO) {
        Conta novaConta = contaService.criarConta(contaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(novaConta)); // Retorna 201 CREATED
    }


    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ContaDTO>> listarContasPorTipo(@PathVariable String tipo) {
        try {
            TipoConta tipoConta = TipoConta.valueOf(tipo.toUpperCase()); // Converte string para enum
            List<Conta> contas = contaService.listarContasPorTipo(String.valueOf(tipoConta));
            List<ContaDTO> response = contas.stream().map(this::convertToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList()); // Retorna 400 se tipo for inválido
        }
    }

    @GetMapping("/{tipo}")
    public ResponseEntity<List<Conta>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(contaService.listarContasPorTipo(tipo));
    }

    private ContaDTO convertToDTO(Conta conta) {
        return new ContaDTO(
                conta.getId(),
                conta.getDescricao(),
                conta.getValor(),
                conta.getTipo(),
                conta.getStatus(),
                conta.getCategoria() != null ? conta.getCategoria().getId() : null,
                conta.getUsuario().getId()
        );
    }
}