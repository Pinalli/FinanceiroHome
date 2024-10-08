package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.ComprasDTO;
import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.exception.CompraNotFoundException;
import br.com.pinalli.financeirohome.model.Compras;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.repository.ComprasRepository;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Autowired
    private ComprasRepository comprasRepository;
    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;
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

        if (comprasDTO == null || comprasDTO.getCartaoCredito() == null || comprasDTO.getCartaoCredito().getId() == null) {
            throw new IllegalArgumentException("Dados inválidos para criar a compra.");
        }

        Long idUsuario = obterIdUsuario(authentication);
        if (idUsuario == null) {
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
        log.debug("Iniciando listarComprasPorCartao no serviço para cartaoId: {}", cartaoId);

        if (cartaoId == null) {
            log.error("ID do cartão é nulo");
            throw new IllegalArgumentException("ID do cartão não pode ser nulo");
        }

        Long idUsuario;
        try {
            idUsuario = obterIdUsuario(authentication);
            log.debug("ID do usuário obtido: {}", idUsuario);
        } catch (Exception e) {
            log.error("Erro ao obter ID do usuário: ", e);
            throw new RuntimeException("Erro ao obter ID do usuário", e);
        }

        Optional<CartaoCredito> cartao;
        try {
            cartao = cartaoCreditoRepository.findById(cartaoId);
            log.debug("Cartão encontrado: {}", cartao.isPresent());
        } catch (Exception e) {
            log.error("Erro ao buscar cartão de crédito: ", e);
            throw new RuntimeException("Erro ao buscar cartão de crédito", e);
        }

        if (cartao.isEmpty()) {
            log.error("Cartão de crédito não encontrado para id: {}", cartaoId);
            throw new CartaoCreditoException("Cartão de crédito não encontrado.");
        }

        if (!Objects.equals(cartao.get().getUsuario().getId(), idUsuario)) {
            log.error("Usuário {} não autorizado a acessar o cartão {}", idUsuario, cartaoId);
            throw new SecurityException("Usuário não autorizado a acessar esta lista.");
        }

        List<Compras> compras;
        try {
            compras = comprasRepository.findByCartaoCreditoIdAndUsuarioId(cartaoId, idUsuario);
            log.debug("Compras encontradas: {}", compras.size());
        } catch (Exception e) {
            log.error("Erro ao buscar compras: ", e);
            throw new RuntimeException("Erro ao buscar compras", e);
        }

        return compras.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    private Long obterIdUsuario(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails =
                    (org.springframework.security.core.userdetails.User) principal;
            String email = userDetails.getUsername();  // Aqui o "username" na autenticação será o email

            // Agora busca o usuário pelo email
            return usuarioService.obterIdPorEmail(email);
        } else {
            throw new SecurityException("Tipo de usuário não suportado: " + principal.getClass().getName());
        }
    }


    public ComprasDTO buscarCompraPorId(Long compraId) {
        Optional<Compras> compra = comprasRepository.findById(compraId);
        if (compra.isEmpty()) {
            throw new CompraNotFoundException();
        }
        return converterParaDTO(compra.get()); //Correção: Convertendo para DTO
    }


    public ComprasDTO atualizarCompra(ComprasDTO compraAtualizada) {
        if (comprasRepository.existsById(compraAtualizada.getId())) {
            Compras compra = compraAtualizada.toEntity();
            Compras compraSalva = comprasRepository.save(compra);
            return ComprasDTO.fromEntity(compraSalva);
        }
        return null;
    }

    public boolean deletarCompra(Long compraId, Authentication authentication) {
        try {
            //Obtém o ID do usuário logado.
            Long idUsuario = obterIdUsuario(authentication);

            if (idUsuario == null) throw new SecurityException("Erro ao obter o ID do usuário.");

            // Busca a compra
            Optional<Compras> compra = comprasRepository.findByIdAndUsuarioId(compraId, idUsuario);

            if (compra.isEmpty()) {
                throw new CompraNotFoundException("Compra não encontrada para o usuário."); // Erro mais específico
            }

            comprasRepository.deleteById(compraId);
            return true;

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Erro ao excluir compra: Problema de integridade de dados.", e);
        } catch (SecurityException | UsernameNotFoundException |
                 IllegalArgumentException e) { //Tratamento mais abrangente de exceções.
            throw new RuntimeException("Erro ao excluir a compra: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao excluir a compra: " + e.getMessage(), e);
        }
    }
}