package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ContaDTO;
import br.com.pinalli.financeirohome.model.Conta;
import br.com.pinalli.financeirohome.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping("/contas")
    public ResponseEntity<ContaDTO> criarConta(@RequestBody ContaDTO contaDTO) {
        Conta novaConta = contaService.criarConta(contaDTO);
        ContaDTO responseDTO = convertToDTO(novaConta);
        return ResponseEntity.ok(responseDTO);

    }
}