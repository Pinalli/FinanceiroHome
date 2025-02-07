package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.dto.ComprasDTO;
import br.com.pinalli.financeirohome.exception.CompraNotFoundException;
import br.com.pinalli.financeirohome.exception.CompraValidationException;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.Compras;
import br.com.pinalli.financeirohome.model.Parcelas;
import br.com.pinalli.financeirohome.repository.ComprasRepository;
import br.com.pinalli.financeirohome.service.CartaoCreditoService;
import br.com.pinalli.financeirohome.service.ComprasService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {


    private static final Logger log = LoggerFactory.getLogger(ComprasService.class); // For SLF4j

    @Autowired
    private ModelMapper modelMapper;
    private final ComprasService comprasService;
    private final CartaoCreditoService cartaoCreditoService;
    private final ComprasRepository comprasRepository;
    private Compras compras;

    @Autowired
    public ComprasController( ComprasService comprasService, CartaoCreditoService cartaoCreditoService, ComprasRepository comprasRepository) {
        this.comprasService = comprasService;
        this.cartaoCreditoService = cartaoCreditoService;
        this.comprasRepository = comprasRepository;
    }

    @PostMapping("/{cartaoId}/compras")
    public ResponseEntity<ComprasDTO> registrarCompra(@PathVariable Long cartaoId, @Valid @RequestBody ComprasDTO comprasDTO, Authentication authentication) {
        try {
            log.debug("Iniciando registro de compra para cartão ID: {}", cartaoId);

            // 1. Buscar o cartão de crédito pelo ID
            CartaoCredito cartaoCredito = cartaoCreditoService.findById(cartaoId);
            if (cartaoCredito == null) {
                log.error("Cartão de crédito não encontrado para o ID: {}", cartaoId);
                return ResponseEntity.notFound().build();
            }

            // 2. Validações básicas
            if (comprasDTO.getDescricao() == null || comprasDTO.getDescricao().trim().isEmpty()) {
                log.error("Descrição da compra é obrigatória.");
                throw new IllegalArgumentException("Descrição da compra é obrigatória.");
            }

            if (comprasDTO.getValor() == null || comprasDTO.getValor().compareTo(BigDecimal.ZERO) <= 0) {
                log.error("Valor da compra é obrigatório e deve ser maior que zero.");
                throw new IllegalArgumentException("Valor da compra é obrigatório e deve ser maior que zero.");
            }

            // 3. Calcular valores importantes
            BigDecimal valorParcela = calcularValorParcela(comprasDTO.getValor(), comprasDTO.getParcelas());
            BigDecimal limiteDisponivelMomentoCompra = calcularLimiteDisponivel(comprasDTO.getValor(), cartaoCredito);

            // 4. Preparar a compra
            comprasDTO.setCartaoCredito(cartaoCredito);
            comprasDTO.setUsuarioId(cartaoCredito.getUsuario().getId());
            comprasDTO.setValorParcela(valorParcela);
            comprasDTO.setLimiteDisponivelMomentoCompra(limiteDisponivelMomentoCompra);
            comprasDTO.setParcelasPagas(0); // Inicializa com zero parcelas pagas

            // Criar lista de parcelas
            List<Parcelas> parcelas = new ArrayList<>();
            LocalDate dataCompra = comprasDTO.getDataCompra();

            for (int i = 1; i <= comprasDTO.getParcelas(); i++) {
                Parcelas parcela = Parcelas.builder()
                        .numeroParcela(i)
                        .valorParcela(valorParcela)
                        .dataVencimento(dataCompra.plusMonths(i-1))
                        .status(Parcelas.StatusParcela.PENDENTE)
                        .build();
                parcelas.add(parcela);
            }

            comprasDTO.setListaParcelas(parcelas);

            // 5. Registrar a compra
            ComprasDTO compraRegistrada = comprasService.registrarCompra(comprasDTO, cartaoId, authentication);

            log.info("Compra registrada com sucesso para o cartão ID: {}", cartaoId);
            return ResponseEntity.ok(compraRegistrada);

        } catch (IllegalArgumentException e) {
            log.error("Erro de argumento inválido ao registrar compra", e);
            return ResponseEntity.badRequest().body(null);
        } catch (DataIntegrityViolationException e) {
            log.error("Erro de integridade de dados ao registrar compra", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            log.error("Erro inesperado ao registrar compra", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/{compraId}/pagar-parcela/{numeroParcela}")
    public ResponseEntity<Void> pagarParcela(@PathVariable Long compraId, @PathVariable Integer numeroParcela) {
        //  comprasService.pagarParcela(compraId, numeroParcela);
        return ResponseEntity.ok().build();
    }

    // Método para calcular o valor da parcela
    private BigDecimal calcularValorParcela(BigDecimal valorTotal, Integer numeroParcelas) {
        return valorTotal.divide(new BigDecimal(numeroParcelas), RoundingMode.HALF_EVEN);
    }

    // Método para calcular o limite disponível no momento da compra
    private BigDecimal calcularLimiteDisponivel(BigDecimal valorCompra, CartaoCredito cartaoCredito) {
        BigDecimal limiteDisponivel = cartaoCredito.getLimiteDisponivel(); // retrieve the credit card limit

        // Calculate the already used amount (valorJaUsed)
        List<Compras> compras = comprasRepository.findByCartaoCredito(cartaoCredito); // fetch all previous purchases for this credit card
        BigDecimal valorJaUsed = BigDecimal.ZERO;
        for (Compras compra : compras) {
            valorJaUsed = valorJaUsed.add(compra.getValor()); // sum up all previous purchase values
        }
        if (limiteDisponivel != null) {
            limiteDisponivel = limiteDisponivel.subtract(valorJaUsed); // calculate available limit
            return limiteDisponivel.subtract(valorCompra); // subtract the new purchase value from the available limit
        } else {
            log.error("Limite de crédito não encontrado");
            throw new RuntimeException("Limite de crédito não encontrado");
        }
    }

    /**
     * catch (SecurityException e) {
     * log.error("Erro de segurança ao registrar compra", e);
     * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
     * <p>
     * } catch (IllegalArgumentException e) {
     * log.error("Erro nos dados de entrada: {}", e.getMessage());
     * return ResponseEntity.badRequest().body(null);
     * <p>
     * } catch (Exception e) {
     * log.error("Erro inesperado ao registrar compra", e);
     * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
     *
     * @param cartaoId
     * @param authentication
     * @return
     **/


    @GetMapping("/{cartaoId}/compras")
    public ResponseEntity<List<ComprasDTO>> listarComprasPorCartao(@PathVariable Long cartaoId, Authentication authentication) {
        try {
            log.debug("Iniciando listarComprasPorCartao para cartaoId: {}", cartaoId);
            List<ComprasDTO> compras = comprasService.listarComprasPorCartao(cartaoId, authentication);
            log.debug("Compras recuperadas: {}", compras);
            return ResponseEntity.ok(compras);
        } catch (Exception e) {
            log.error("Erro ao listar compras para o cartão {}: ", cartaoId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private void verificarAutenticacao(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }
    }

    @GetMapping("/{cartaoId}/compra/{compraId}")
    public ResponseEntity<ComprasDTO> buscarCompra(@PathVariable Long cartaoId, @PathVariable Long compraId) {
        if (cartaoId == null || cartaoId <= 0 || compraId == null || compraId <= 0) {
            throw new CompraValidationException("ID do cartão ou da compra inválido.");
        }
        ComprasDTO compra = comprasService.buscarCompraPorId(compraId);
        if (compra == null || !compra.getCartaoCredito().getId().equals(cartaoId)) {
            throw new CompraNotFoundException(compraId);
        }
        return ResponseEntity.ok(compra);
    }

    @PutMapping("/{compraId}")
    public ResponseEntity<ComprasDTO> atualizarCompra(
            @PathVariable Long compraId,
            @Valid @RequestBody ComprasDTO compraAtualizada,
            Authentication authentication) {

        try {
            verificarAutenticacao(authentication);
            compraAtualizada.setId(compraId);
            ComprasDTO compraDTO = comprasService.atualizarCompra(compraAtualizada, authentication);
            if (compraDTO == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(compraDTO);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("/{compraId}")
    public ResponseEntity<Void> deletarCompra(@PathVariable Long compraId, Authentication authentication) {
        try {
            verificarAutenticacao(authentication);
            if (comprasService.deletarCompra(compraId, authentication)) {
                return ResponseEntity.noContent().build(); // Status 204
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //Tratamento adequado de erros
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}