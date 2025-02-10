package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ContaReceberDTO;

import br.com.pinalli.financeirohome.exception.ContaReceberException;
import br.com.pinalli.financeirohome.service.ContaReceberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/conta-a-receber")
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
    public ResponseEntity<?> atualizarContaReceber(@PathVariable Long id, @Valid @RequestBody ContaReceberDTO contaReceberDTO) {
        try {
            ContaReceberDTO contaAtualizada = contaReceberService.atualizarContaReceber(id, contaReceberDTO);
            return ResponseEntity.ok(contaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar conta a receber: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirContaReceber(@PathVariable Long id, Authentication authentication) {
        try {
            if (contaReceberService.excluirContaReceber(id, authentication)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (ContaReceberException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
