package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ContaReceberDTO;

import br.com.pinalli.financeirohome.service.ContaReceberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/contas-a-receber")
public class ContaReceberController {

    @Autowired
    private ContaReceberService contaReceberService;


    @PostMapping
    public ResponseEntity<ContaReceberDTO> criarContaReceber(@Valid @RequestBody ContaReceberDTO contaReceberDTO) {
        if (contaReceberDTO.getUsuarioId() == null &&
                (contaReceberDTO.getUsuarioDTO() == null || contaReceberDTO.getUsuarioDTO().getId() == null)) {
            throw new IllegalArgumentException("Informações do usuário não fornecidas.");
        }
        return ResponseEntity.ok(contaReceberService.criarContaReceber(contaReceberDTO));
    }

    @GetMapping
    public ResponseEntity<List<ContaReceberDTO>> listarContasReceber() {
        List<ContaReceberDTO> contasDTO = contaReceberService.listarContasReceber();
        return ResponseEntity.ok(contasDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaReceberDTO> buscarContaReceber(@PathVariable Long id) {
        ContaReceberDTO contaReceberDTO = contaReceberService.buscarContaReceber(id);
        return ResponseEntity.ok(contaReceberDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaReceberDTO> atualizarContaReceber(
            @PathVariable Long id,
            @Valid @RequestBody ContaReceberDTO contaReceberDTO) {
        ContaReceberDTO updatedContaReceberDTO = contaReceberService.atualizarContaReceber(id, contaReceberDTO);
        return ResponseEntity.ok(updatedContaReceberDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarContaReceber(@PathVariable Long id) {
        contaReceberService.deletarContaReceber(id);
        return ResponseEntity.noContent().build();
    }
}
