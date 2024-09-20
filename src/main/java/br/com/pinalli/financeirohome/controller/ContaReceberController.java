package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ContaReceberDTO;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import br.com.pinalli.financeirohome.model.ContaReceber;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import br.com.pinalli.financeirohome.service.ContaReceberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contas-a-receber")
public class ContaReceberController {

    @Autowired
    private ContaReceberService contaReceberService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<ContaReceberDTO> criarContaReceber(@RequestBody
                                                                 ContaReceberDTO contaReceberDTO) {
        ContaReceberDTO novaContaDTO = contaReceberService.criarContaReceber(contaReceberDTO); // Passar o DTO para o service
        return ResponseEntity.status(HttpStatus.CREATED).body(novaContaDTO);
    }
    //convert DTO
    private ContaReceberDTO convertToDto(ContaReceber novaConta) {
        ContaReceberDTO novaContaDTO = new ContaReceberDTO();
        novaContaDTO.setId(novaConta.getId());
        novaContaDTO.setDescricao(novaConta.getDescricao());
        novaContaDTO.setValor(novaConta.getValor()); // Corrigido: usar valor da entidade
        novaContaDTO.setDataRecebimento(novaConta.getDataRecebimento()); // Corrigido: usar data da entidade
        novaContaDTO.setStatus(novaConta.getStatus());
        novaContaDTO.setCategoria(novaConta.getCategoria());
        novaContaDTO.setUsuarioDTO(UsuarioDTO.fromUsuario(novaConta.getUsuario())); // Corrigido: mapear Usuario para UsuarioDTO
        return novaContaDTO;
    }
    //convert Entity
    private ContaReceber convertToEntity(ContaReceberDTO contaReceberDTO) {
        ContaReceber contaReceber = new ContaReceber();
        contaReceber.setId(contaReceberDTO.getId());
        contaReceber.setDescricao(contaReceberDTO.getDescricao());
        contaReceber.setValor(contaReceberDTO.getValor());
        contaReceber.setDataRecebimento(contaReceberDTO.getDataRecebimento());
        contaReceber.setStatus(contaReceberDTO.getStatus());
        contaReceber.setCategoria(contaReceberDTO.getCategoria());
        contaReceber.setUsuario(usuarioRepository.findById(contaReceberDTO.getUsuarioDTO().getId()).orElse(null));
        return contaReceber;
    }

    @GetMapping
    public ResponseEntity<List<ContaReceberDTO>> listarContasReceber() {
        List<ContaReceberDTO> contasDTO = contaReceberService.listarContasReceber();
        return ResponseEntity.ok(contasDTO);
    }


    // Adicione outros métodos para atualizar, excluir, etc. conforme necessário
}

