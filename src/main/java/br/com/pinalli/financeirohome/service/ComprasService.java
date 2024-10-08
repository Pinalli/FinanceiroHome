package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.ComprasDTO;
import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.model.Compras;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.ComprasRepository;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import br.com.pinalli.financeirohome.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ComprasService {

    private static final Logger log = LoggerFactory.getLogger(ComprasService.class); // For SLF4j

    private final ComprasRepository comprasRepository;
    private final CartaoCreditoRepository cartaoCreditoRepository;
    @Autowired
    private  UsuarioRepository usuarioRepository;
    //Injeção correta
    @Autowired
    private CartaoCreditoService cartaoCreditoService;

    @Autowired
    public ComprasService(ComprasRepository comprasRepository, CartaoCreditoRepository cartaoCreditoRepository, UsuarioRepository usuarioRepository) {
        this.comprasRepository = comprasRepository;
        this.cartaoCreditoRepository = cartaoCreditoRepository;
        this.usuarioRepository = usuarioRepository;
    }


    @Transactional
    public ComprasDTO registrarCompra(ComprasDTO comprasDTO, Long cartaoId, Authentication authentication) {
        log.info("Registrando compra para o cartão: {}", cartaoId);
        log.info("ComprasDTO recebido: {}", comprasDTO);

        if(comprasDTO == null || comprasDTO.getCartaoCredito() == null || comprasDTO.getCartaoCredito().getId() == null) {
            throw new IllegalArgumentException("Dados inválidos para criar a compra.");
        }

        Long idUsuario = obterIdUsuario(authentication);
        if(idUsuario == null) {
            throw new SecurityException("Erro ao obter ID do usuário.");
        }
        log.debug("ID do usuário autenticado: {}", idUsuario);

        Optional<CartaoCredito> cartaoCredito = cartaoCreditoRepository.findById(cartaoId);
        if (cartaoCredito.isEmpty()) {
            throw new CartaoCreditoException("Cartão de crédito não encontrado.");
        }
        if (!Objects.equals(cartaoCredito.get().getUsuario().getId(), idUsuario)) {
            throw new SecurityException("Usuário não autorizado a criar compras para este cartão.");
        }

        // Definir o usuarioId no DTO do cartão de crédito
        comprasDTO.getCartaoCredito().setUsuarioId(idUsuario);

        try {
            Compras compra = comprasDTO.toEntity();
            log.info("Compra criada: {}", compra);
            compra.setCartaoCredito(cartaoCredito.get());
            compra.setUsuario(cartaoCredito.get().getUsuario());

            log.info("Salvando compra: {}", compra);
            Compras compraSalva = comprasRepository.save(compra);
            log.info("Compra salva com sucesso: {}", compraSalva);

            return ComprasDTO.fromEntity(compraSalva);
        } catch (Exception e) {
            log.error("Erro ao criar compra: ", e);
            throw e;
        }
    }


    private ComprasDTO converterParaDTO(Compras compra) {
        if (compra == null) {
            return null; //Importante: Retorna null caso a compra seja nula
        }

        return ComprasDTO.builder()
                .id(compra.getId())
                .dataCompra(compra.getData())
                .valor(compra.getValor())
                .descricao(compra.getDescricao())
                .categoria(compra.getCategoria())
                .parcelas(compra.getParcelas())
                .parcelasPagas(compra.getParcelasPagas())
                .cartaoCredito(CartaoCreditoDTO.converterParaDTO(compra.getCartaoCredito()))
                .build();
    }


    public List<ComprasDTO> listarComprasPorCartao(Long cartaoId, Authentication authentication) {

        if (cartaoId == null) {
            throw new IllegalArgumentException("ID do cartão inválido.");
        }

        Long idUsuario = cartaoCreditoService.obterIdUsuario(authentication);
        if (idUsuario == null) {
            throw new SecurityException("Erro ao obter o ID do usuário.");
        }

        Optional<CartaoCredito> cartao = cartaoCreditoRepository.findById(cartaoId);


        if (cartao.isEmpty()) {
            throw new CartaoCreditoException("Cartão de crédito não encontrado.");
        }

        if (!Objects.equals(cartao.get().getUsuario().getId(), idUsuario)) {
            throw new SecurityException("Usuário não autorizado a acessar esta lista.");
        }


        List<Compras> compras = comprasRepository.findByCartaoCreditoIdAndUsuarioId(cartaoId, idUsuario);
        if (compras == null || compras.isEmpty()) {
            return List.of(); // Retorna uma lista vazia.
        }


        return compras.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }


    private ComprasDTO converterComprasParaDTOs(Compras compra) {
        return ComprasDTO.fromEntity(compra);
    }


    private Long obterIdUsuario(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        } else if (principal instanceof String email) {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            return usuario.getId();
        } else if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            Usuario usuario = usuarioRepository.findByEmail(springUser.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            return usuario.getId();
        } else {
            log.error("Tipo de usuário não suportado: {}", principal.getClass().getName());
            throw new IllegalStateException("Tipo de usuário não suportado: " + principal.getClass().getName());
        }
    }


    public ComprasDTO buscarCompraPorId(Long compraId) {
        return comprasRepository.findById(compraId)
                .map(ComprasDTO::fromEntity)
                .orElse(null);
    }


    public ComprasDTO atualizarCompra(ComprasDTO compraAtualizada) {
        if (comprasRepository.existsById(compraAtualizada.getId())) {
            Compras compra = compraAtualizada.toEntity();
            Compras compraSalva = comprasRepository.save(compra);
            return ComprasDTO.fromEntity(compraSalva);
        }
        return null;
    }

    @Transactional
    public void deletarCompra(Long compraId) {
        comprasRepository.deleteById(compraId);
    }
}