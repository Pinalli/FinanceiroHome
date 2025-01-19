package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import br.com.pinalli.financeirohome.service.CartaoCreditoService;
import br.com.pinalli.financeirohome.service.TokenService;
import br.com.pinalli.financeirohome.service.UsuarioService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/cartoes-credito")
public class CartaoCreditoController {

    private static final Logger log = LoggerFactory.getLogger(CartaoCreditoController.class);
    private final CartaoCreditoService cartaoCreditoService;
    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final UsuarioRepository userRepository;

    public CartaoCreditoController(CartaoCreditoService cartaoCreditoService,
                                   UsuarioRepository userRepository,
                                   UsuarioService usuarioService,
                                   TokenService tokenService) {
        this.cartaoCreditoService = cartaoCreditoService;
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    private Long obterIdUsuario(Authentication authentication) {
        log.debug("Authentication: {}", authentication);
        log.debug("Principal: {}", authentication.getPrincipal());

        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            log.error("Principal não é uma instância de UserDetails");
            throw new RuntimeException("Tipo de autenticação inválido");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.debug("Username: {}", userDetails.getUsername());

        return usuarioService.findByEmail(userDetails.getUsername())
                .map(usuario -> {
                    Usuario usuarioEspecifico = (Usuario) usuario;
                    log.debug("Usuario encontrado: {}", usuarioEspecifico.getId());
                    return usuarioEspecifico.getId();
                })
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado para o email: {}", userDetails.getUsername());
                    return new RuntimeException("Usuário não encontrado");
                });

    }

    @PostMapping
    public ResponseEntity<?> criarCartaoCredito(@RequestBody CartaoCreditoDTO cartaoCreditoDTO) {
        try {
            // Chama o metodo getUsuarioAutenticado() para obter o usuário logado
            Usuario usuarioAutenticado = usuarioService.getUsuarioAutenticado();

            // Verifica se o usuário autenticado é válido
            if (usuarioAutenticado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuário não autenticado");
            }

            // Atribui o ID do usuário autenticado ao Cartão de Crédito
            cartaoCreditoDTO.setUsuarioId(usuarioAutenticado.getId());

            // Validação manual
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<CartaoCreditoDTO>> violations = validator.validate(cartaoCreditoDTO);

            if (!violations.isEmpty()) {
                List<String> errors = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }

            // Criação do cartão de crédito
            CartaoCreditoDTO novoCartao = cartaoCreditoService.criarCartaoCredito(cartaoCreditoDTO);

            // Retorno da resposta com o cartão criado
            return new ResponseEntity<>(novoCartao, HttpStatus.CREATED);
        } catch (Exception e) {
            // Captura qualquer erro e retorna a resposta de erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar cartão de crédito: " + e.getMessage());
        }
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }


    private void verificarAutenticacao(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Usuário não autenticado.");
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CartaoCreditoDTO>> buscarCartoesPorUsuario(@PathVariable Long usuarioId) {
        log.info("Buscando cartões para o usuário com ID: {}", usuarioId);
        List<CartaoCreditoDTO> cartoes = cartaoCreditoService.buscarCartoesPorUsuario(usuarioId);
        log.info("Cartões encontrados: {}", cartoes);
        return ResponseEntity.ok(cartoes);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CartaoCreditoDTO> buscarCartaoCredito(@PathVariable Long id) {
        CartaoCreditoDTO cartao = cartaoCreditoService.buscarCartaoCreditoPorId(id);
        if (cartao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaoCreditoDTO> atualizarCartaoCredito(@PathVariable Long id, @Valid @RequestBody CartaoCreditoDTO cartaoCreditoDTO) {
        try {
            //Importantíssimo: seta o id do DTO para a requisição
            cartaoCreditoDTO.setId(id);
            CartaoCreditoDTO atualizado = cartaoCreditoService.atualizarCartaoCredito(cartaoCreditoDTO);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (CartaoCreditoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Correção
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorno correto para exceções mais gerais
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCartaoCredito(@PathVariable Long id) {
        cartaoCreditoService.deletarCartaoCredito(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/limite-e-compras")
    public ResponseEntity<CartaoCreditoDTO> obterLimiteEComprasAbertas(@PathVariable Long id) {
        try {
            CartaoCreditoDTO info = cartaoCreditoService.obterLimiteEComprasAbertas(id);
            return ResponseEntity.ok(info);
        } catch (CartaoCreditoException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
