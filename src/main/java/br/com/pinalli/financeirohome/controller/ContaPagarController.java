package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ContaPagarDTO;
import br.com.pinalli.financeirohome.model.ContaPagar;
import br.com.pinalli.financeirohome.repository.ContaPagarRepository;
import br.com.pinalli.financeirohome.service.ContaPagarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contas-a-pagar")
public class ContaPagarController {

    @Autowired
    private ContaPagarService contaPagarService;
    @Autowired
    private ContaPagarRepository contaPagarRepository;

    @PostMapping
    public ResponseEntity<ContaPagar> criarContaPagar(@RequestBody ContaPagar contaPagar) {
        ContaPagar novaConta = contaPagarService.criarContaPagar(contaPagar);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping
    public List<ContaPagarDTO> listarContasPagar() {
        List<ContaPagar> contasPagar = contaPagarRepository.findAll();
        return contasPagar.stream()
                .map(this::convertToDto) // método para converter ContaPagar para ContaPagarDTO
                .collect(Collectors.toList());
    }
    private ContaPagarDTO convertToDto(ContaPagar contaPagar) {
        ContaPagarDTO dto = new ContaPagarDTO();
        dto.setId(contaPagar.getId());
        dto.setDescricao(contaPagar.getDescricao());
        dto.setValor(contaPagar.getValor().doubleValue());
        dto.setDataVencimento(contaPagar.getDataVencimento());
        dto.setStatus(contaPagar.getStatus().name()); // Converter enum para String
        dto.setCategoria(contaPagar.getCategoria()); // Assumindo que Categoria tem um atributo nome
        return dto;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ContaPagar> obterContaPagarPorId(@PathVariable Long id) {
        return contaPagarService.obterContaPagarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ContaPagar> atualizarContaPagar(@PathVariable Long id,
                                                          @RequestBody ContaPagar contaPagar) {
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