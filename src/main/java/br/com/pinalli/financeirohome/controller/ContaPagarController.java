package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.model.ContaPagar;
import br.com.pinalli.financeirohome.service.ContaPagarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas-a-pagar")
public class ContaPagarController {

    @Autowired
    private ContaPagarService contaPagarService;

    @PostMapping
    public ResponseEntity<ContaPagar> criarContaPagar(@RequestBody ContaPagar contaPagar) {
        ContaPagar novaConta = contaPagarService.criarContaPagar(contaPagar);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping
    public ResponseEntity<List<ContaPagar>> listarContasPagar() {
        List<ContaPagar> contas = contaPagarService.listarContasPagar();
        return ResponseEntity.ok(contas);
    }

    // Adicione outros métodos para atualizar, excluir, etc. conforme necessário
}