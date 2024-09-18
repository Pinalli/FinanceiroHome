package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.model.ContaReceber;
import br.com.pinalli.financeirohome.service.ContaReceberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas-a-receber")
public class ContaReceberController {

    @Autowired
    private ContaReceberService contaReceberService;

    @PostMapping
    public ResponseEntity<ContaReceber> criarContaReceber(@RequestBody ContaReceber contaReceber) {
        ContaReceber novaConta = contaReceberService.criarContaReceber(contaReceber);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping
    public ResponseEntity<List<ContaReceber>> listarContasReceber() {
        List<ContaReceber> contas = contaReceberService.listarContasReceber();
        return ResponseEntity.ok(contas);
    }

    // Adicione outros métodos para atualizar, excluir, etc. conforme necessário
}