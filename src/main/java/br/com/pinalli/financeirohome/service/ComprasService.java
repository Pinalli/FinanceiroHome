package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.ComprasDTO;
import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.exception.CompraNotFoundException;
import br.com.pinalli.financeirohome.model.Compras;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.Parcelas;
import br.com.pinalli.financeirohome.repository.ComprasRepository;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ComprasService {

    private static final Logger log = LoggerFactory.getLogger(ComprasService.class); // For SLF4j

    private final ComprasRepository comprasRepository;
    private final CartaoCreditoRepository cartaoCreditoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final CartaoCreditoService cartaoCreditoService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public ComprasService(ComprasRepository comprasRepository,
                          CartaoCreditoRepository cartaoCreditoRepository,
                          UsuarioRepository usuarioRepository,
                          UsuarioService usuarioService,
                          CartaoCreditoService cartaoCreditoService) {
        this.comprasRepository = comprasRepository;
        this.cartaoCreditoRepository = cartaoCreditoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.cartaoCreditoService = cartaoCreditoService;
    }
    @Transactional
    public ComprasDTO registrarCompra(ComprasDTO comprasDTO, Long cartaoId, Authentication authentication) {
        log.info("Registrando compra para o cartão: {}", cartaoId);

        // Validações iniciais
        if (comprasDTO == null || comprasDTO.getCartaoCredito() == null) {
            throw new IllegalArgumentException("Dados inválidos para criar a compra.");
        }

        // Obter usuário
        Long idUsuario = obterIdUsuario(authentication);
        if (idUsuario == null) {
            throw new SecurityException("Erro ao obter ID do usuário.");
        }

        // Obter cartão de crédito
        Optional<CartaoCredito> cartaoCreditoOpt = cartaoCreditoRepository.findById(cartaoId);
        if (cartaoCreditoOpt.isEmpty()) {
            throw new CartaoCreditoException("Cartão de crédito não encontrado.");
        }
        CartaoCredito cartaoCredito = cartaoCreditoOpt.get();

        try {
            // Calcular valor da parcela
            BigDecimal valorParcela = comprasDTO.getValor()
                    .divide(BigDecimal.valueOf(comprasDTO.getParcelas()), 2, RoundingMode.HALF_UP);

            // Criar a compra
            Compras compra = Compras.builder()
                    .dataCompra(comprasDTO.getDataCompra().atStartOfDay())
                    .valor(comprasDTO.getValor())
                    .descricao(comprasDTO.getDescricao())
                    .categoria(comprasDTO.getCategoria())
                    .parcelas(comprasDTO.getParcelas())  // Campo que estava faltando
                    .parcelasPagas(0)
                    .cartaoCredito(cartaoCredito)
                    .usuario(cartaoCredito.getUsuario())
                    .status(Compras.StatusCompra.PENDENTE)
                    .valorParcela(valorParcela)
                    .limiteDisponivelMomentoCompra(cartaoCreditoService.getLimiteDisponivel(cartaoId))
                    .dataLimite(comprasDTO.getDataCompra().plusMonths(comprasDTO.getParcelas()).atStartOfDay())
                    .build();

            // Criar parcelas
            for (int i = 1; i <= comprasDTO.getParcelas(); i++) {
                Parcelas parcela = Parcelas.builder()
                        .numeroParcela(i)
                        .valorParcela(valorParcela)
                        .dataVencimento(comprasDTO.getDataCompra().plusMonths(i-1))
                        .status(Parcelas.StatusParcela.PENDENTE)
                        .compra(compra)
                        .build();
                compra.adicionarParcela(parcela);
            }

            // Salvar a compra
            Compras compraSalva = comprasRepository.save(compra);

            // Atualizar limite do cartão
            cartaoCreditoService.atualizarLimiteEComprasAbertas(cartaoId, compra.getValor(), true);

            return ComprasDTO.fromEntity(compraSalva);
        } catch (Exception e) {
            log.error("Erro ao criar compra: ", e);
            throw new RuntimeException("Erro ao criar compra: " + e.getMessage(), e);
        }
    }

    private ComprasDTO converterParaDTO(Compras compra) {
        if (compra == null) {
            return null; //Importante: Retorna null caso a compra seja nula
        }

        return ComprasDTO.builder()
                .id(compra.getId())
                .dataCompra(LocalDate.from(compra.getDataCompra()))
                .valor(compra.getValor())
                .descricao(compra.getDescricao())
                .categoria(compra.getCategoria())
                //    .parcelas(compra.getParcelas())
                .cartaoCredito(CartaoCreditoDTO.converterParaDTO(compra.getCartaoCredito()))
                .build();
    }

    @Transactional
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

        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            String email = userDetails.getUsername();  // Aqui o "username" na autenticação será o email

            // Agora busca o usuário pelo email
            return usuarioService.obterIdPorEmail(email);
        } else {
            throw new SecurityException("Tipo de usuário não suportado: " + principal.getClass().getName());
        }
    }

    @Transactional
    public ComprasDTO buscarCompraPorId(Long compraId) {
        Optional<Compras> compra = comprasRepository.findById(compraId);
        if (compra.isEmpty()) {
            throw new CompraNotFoundException();
        }
        return converterParaDTO(compra.get()); //Correção: Convertendo para DTO
    }


    @Transactional
    public ComprasDTO atualizarCompra(ComprasDTO compraAtualizada, Authentication authentication) {
        log.debug("Iniciando atualização da compra: {}", compraAtualizada);

        if (compraAtualizada == null || compraAtualizada.getId() == null || compraAtualizada.getCartaoCredito() == null || compraAtualizada.getCartaoCredito().getId() == null) {
            throw new IllegalArgumentException("Dados inválidos para atualizar a compra.");
        }

        Long idUsuario = obterIdUsuario(authentication);
        if (idUsuario == null) {
            throw new SecurityException("Erro ao obter o ID do usuário.");
        }

        Optional<Compras> compraExistenteOpt = comprasRepository.findById(compraAtualizada.getId());
        if (compraExistenteOpt.isEmpty()) {
            throw new CompraNotFoundException("Compra não encontrada para o ID fornecido.");
        }

        Compras compraExistente = compraExistenteOpt.get();

        Optional<CartaoCredito> cartao = cartaoCreditoRepository.findById(compraAtualizada.getCartaoCredito().getId());
        if (cartao.isEmpty()) {
            throw new CartaoCreditoException("Cartão de crédito não encontrado.");
        }

        if (!Objects.equals(cartao.get().getUsuario().getId(), idUsuario) || !Objects.equals(compraExistente.getUsuario().getId(), idUsuario)) {
            throw new SecurityException("Usuário não autorizado a atualizar esta compra.");
        }

        try {
            BigDecimal valorOriginal = compraExistente.getValor();

            compraExistente.setDataCompra(compraAtualizada.getDataCompra().atStartOfDay());
            compraExistente.setValor(compraAtualizada.getValor());
            compraExistente.setDescricao(compraAtualizada.getDescricao());
            compraExistente.setCategoria(compraAtualizada.getCategoria());
            //     compraExistente.setParcelas(compraAtualizada.getParcelas());
            //    compraExistente.setParcelasPagas(compraAtualizada.getParcelasPagas());
            compraExistente.setCartaoCredito(cartao.get());

            Compras compraSalva = comprasRepository.save(compraExistente);
            log.debug("Compra atualizada com sucesso: {}", compraSalva);

            BigDecimal diferencaValor = compraAtualizada.getValor().subtract(valorOriginal);
            cartaoCreditoService.atualizarLimiteEComprasAbertas(cartao.get().getId(), diferencaValor, false);

            return ComprasDTO.fromEntity(compraSalva);
        } catch (DataIntegrityViolationException e) {
            log.error("Erro de integridade de dados ao atualizar compra", e);
            throw new RuntimeException("Erro ao atualizar compra: Problema de integridade de dados.", e);
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar compra", e);
            throw new RuntimeException("Erro inesperado ao atualizar compra: " + e.getMessage(), e);
        }
    }

    @Transactional
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

            Long cartaoId = compra.get().getCartaoCredito().getId();  // Obtendo o cartaoId
            BigDecimal valorCompra = compra.get().getValor();          // Obtendo o valor da compra

            // Exclui a compra
            comprasRepository.deleteById(compraId);

            // Atualizar o limite disponível e total de compras abertas do cartão
            cartaoCreditoService.cancelarCompra(cartaoId, valorCompra);

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