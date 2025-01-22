package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ContaPagarDTO;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import br.com.pinalli.financeirohome.model.ContaPagar;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.ContaPagarRepository;
import br.com.pinalli.financeirohome.service.ContaPagarService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contas-a-pagar")
public class ContaPagarController {

    private static final Logger logger = LoggerFactory.getLogger(ContaPagarController.class);

    private final ContaPagarService contaPagarService;
    private final ContaPagarRepository contaPagarRepository;

    public ContaPagarController(ContaPagarService contaPagarService, ContaPagarRepository contaPagarRepository) {
        this.contaPagarService = contaPagarService;
        this.contaPagarRepository = contaPagarRepository;
    }

    private void verificarAutenticacao() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }
    }

    @PostMapping
    public ResponseEntity<ContaPagarDTO> criarContaPagar(@Valid @RequestBody ContaPagarDTO contaPagarDTO) {
        verificarAutenticacao();
        logger.info("Iniciando criação da conta a pagar");
            try {
                logger.info("Dados recebidos: {}", contaPagarDTO);
                ContaPagar conta = contaPagarService.converterDtoParaEntidade(contaPagarDTO);
                ContaPagar novaConta = contaPagarService.criarContaPagar(conta);
                ContaPagarDTO contaDTO = contaPagarService.converterParaDTO(novaConta);
                logger.info("Conta a pagar criada com sucesso");
                return new ResponseEntity<>(contaDTO, HttpStatus.CREATED); // Retorna o DTO
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null);
            } catch (SecurityException e){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }catch(Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }


    private ContaPagarDTO converterParaDTO(ContaPagar contaPagar) {
        if (contaPagar == null) return null;

        return ContaPagarDTO.builder()
                .id(contaPagar.getId())
                .descricao(contaPagar.getDescricao())
                .valor(contaPagar.getValor())
                .dataVencimento(contaPagar.getDataVencimento())
                .status(contaPagar.getStatus().name())
                .categoria(contaPagar.getCategoria())
                .usuario(converterUsuarioParaDTO(contaPagar.getUsuario())) // Chamada ao método correto
                .build();
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
        if (contaPagar == null) return null;

        return ContaPagarDTO.builder()
                .id(contaPagar.getId())
                .descricao(contaPagar.getDescricao())
                .valor(contaPagar.getValor())
                .dataVencimento(contaPagar.getDataVencimento())
                .status(contaPagar.getStatus().name())
                .categoria(contaPagar.getCategoria())
                .usuario(UsuarioDTO.fromUsuario(contaPagar.getUsuario())) // Corrigido
                .build();
    }

    private UsuarioDTO converterUsuarioParaDTO(Usuario usuario) {
        return UsuarioDTO.fromUsuario(usuario);
    }


    @GetMapping("/usuario")
    public ResponseEntity<List<ContaPagarDTO>> listarContasPagarDoUsuario(Authentication authentication){

        try {
            List<ContaPagarDTO> contasDTO = contaPagarService.listarContasPagarDoUsuario(authentication);
            return ResponseEntity.ok(contasDTO);
        }
        catch (SecurityException | IllegalArgumentException ex){
            // Retorna uma resposta de erro com uma mensagem mais específica
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<ContaPagar> obterContaPagarPorId(@PathVariable Long id) {
        try {
            Optional<ContaPagar> conta = contaPagarService.obterContaPagarPorId(id);

            return conta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();  //Retorne um 400 para dados inválidos
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorne 500 para erro interno
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaPagar> atualizarContaPagar(@PathVariable Long id,
                                                          @RequestBody ContaPagar contaPagar) {
        try {
            Optional<ContaPagar> updatedConta = contaPagarService.atualizarContaPagar(id, contaPagar);
            return updatedConta.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) { //Tratamento de exceções mais genéricas
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirContaPagar(@PathVariable Long id) {
        try {
            if (contaPagarService.excluirContaPagar(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Trate exceções (ex: banco de dados, etc)
            System.err.println("Erro ao excluir a conta a pagar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorno adequado
        }
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}