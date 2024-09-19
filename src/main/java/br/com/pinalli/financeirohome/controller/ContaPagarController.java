package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ContaPagarDTO;
import br.com.pinalli.financeirohome.model.ContaPagar;
import br.com.pinalli.financeirohome.model.StatusConta;
import br.com.pinalli.financeirohome.repository.ContaPagarRepository;
import br.com.pinalli.financeirohome.service.ContaPagarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public ResponseEntity<ContaPagarDTO> criarContaPagar(@RequestBody ContaPagarDTO contaPagarDTO) {
        ContaPagar contaPagar = convertToEntity(contaPagarDTO); // Converter DTO para entidade
        ContaPagar novaConta = contaPagarService.criarContaPagar(contaPagar);
        ContaPagarDTO novaContaDTO = convertToDto(novaConta); // Converter entidade para DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(novaContaDTO);
    }

    @GetMapping
    //Inicia o fluxo de dados (stream) a partir da lista de contas a pagar
    public List<ContaPagarDTO> listarContasPagar() {
        // Busca todas as contas a pagar do banco de dados através do repositório
        List<ContaPagar> contasPagar = contaPagarRepository.findAll();
        // Converte a lista de ContaPagar para uma lista de ContaPagarDTO
        return contasPagar.stream()// Inicia o fluxo de dados (stream) a partir da lista de contas a pagar
                .map(this::convertToDto) // Aplica o método 'convertToDto' para cada item da lista,
                // transformando de ContaPagar para ContaPagarDTO
                .collect(Collectors.toList());// Coleta o resultado e converte de volta para uma lista de ContaPagarDTO

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


    private ContaPagar convertToEntity(ContaPagarDTO contaPagarDTO) {
        ContaPagar contaPagar = new ContaPagar();
        contaPagar.setId(contaPagarDTO.getId());
        contaPagar.setDescricao(contaPagarDTO.getDescricao());
        contaPagar.setValor(BigDecimal.valueOf(contaPagarDTO.getValor())); // Converter Double para BigDecimal
        contaPagar.setDataVencimento(contaPagarDTO.getDataVencimento());
        contaPagar.setStatus(StatusConta.valueOf(contaPagarDTO.getStatus())); // Converter String para enum
        contaPagar.setCategoria(contaPagarDTO.getCategoria());
        // ... mapear outros atributos, se necessário ...
        return contaPagar;
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