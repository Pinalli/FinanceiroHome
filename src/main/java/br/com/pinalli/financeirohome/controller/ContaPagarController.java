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
    @GetMapping("/{id}")
    public ResponseEntity<ContaPagar> obterContaPagarPorId(@PathVariable Long id) {
        return contaPagarService.obterContaPagarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ContaPagar> atualizarContaPagar(@PathVariable Long id, @RequestBody ContaPagar contaPagar) {
        return contaPagarService.atualizarContaPagar(id, contaPagar)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirContaPagar(@PathVariable Long id) {
        if (contaPagarService.excluirContaPagar(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}