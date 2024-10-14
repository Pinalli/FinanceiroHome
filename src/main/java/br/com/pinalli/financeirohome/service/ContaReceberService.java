package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.ContaReceberDTO;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import br.com.pinalli.financeirohome.exception.CartaoCreditoException;
import br.com.pinalli.financeirohome.model.ContaReceber;
import br.com.pinalli.financeirohome.model.Usuario; // Ajuste o caminho conforme necessário
import br.com.pinalli.financeirohome.repository.ContaReceberRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaReceberService {

    private final Logger logger;
    private final ContaReceberRepository contaReceberRepository;
    @Autowired
    private final UsuarioRepository usuarioRepository;


    public ContaReceberService(ContaReceberRepository contaReceberRepository, UsuarioRepository usuarioRepository) {
        this.contaReceberRepository = contaReceberRepository;
        this.usuarioRepository = usuarioRepository;
        this.logger = LoggerFactory.getLogger(ContaReceberService.class);
    }

    public ContaReceberDTO criarContaReceber(ContaReceberDTO contaReceberDTO) {
        if (contaReceberDTO == null || contaReceberDTO.getDescricao() == null || contaReceberDTO.getDescricao().isEmpty()) {
            throw new IllegalArgumentException("Dados inválidos para a conta a receber.");
        }

        Usuario usuario;
        if (contaReceberDTO.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(contaReceberDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        } else if (contaReceberDTO.getUsuarioDTO() != null && contaReceberDTO.getUsuarioDTO().getId() != null) {
            usuario = usuarioRepository.findById(contaReceberDTO.getUsuarioDTO().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        } else {
            throw new IllegalArgumentException("Informações do usuário não fornecidas.");
        }

        ContaReceber novaConta = converterDtoParaEntidade(contaReceberDTO);
        novaConta.setUsuario(usuario);

        ContaReceber contaSalva = contaReceberRepository.save(novaConta);
        return converterEntidadeParaDto(contaSalva);
    }

    public List<ContaReceberDTO> listarContasReceber () {
        logger.info("Iniciando a lista das contas recebidas.");
        // Busca todas as contas a receber do banco de dados através do repositório
        List<ContaReceber> contasReceber = contaReceberRepository.findAll();

        // Converte a lista de ContaReceber para uma lista de ContaReceberDTO
        return contasReceber.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ContaReceberDTO buscarContaReceber (Long id){
        Optional<ContaReceber> contaReceberOptional = obterContaReceberPorId(id);
        return contaReceberOptional.map(this::convertToDto).orElseThrow(() -> new ServiceException("Conta a receber não encontrada"));
    }

    public Optional<ContaReceber> obterContaReceberPorId (Long id){
        return contaReceberRepository.findById(id);
    }

    public ContaReceberDTO atualizarContaReceber(Long id, ContaReceberDTO contaReceberDTO) {
        ContaReceber contaExistente = contaReceberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a receber não encontrada."));

        // Atualizar campos
        contaExistente.setDescricao(contaReceberDTO.getDescricao());
        contaExistente.setValor(contaReceberDTO.getValor());
        contaExistente.setDataRecebimento(contaReceberDTO.getDataRecebimento());
        contaExistente.setStatus(contaReceberDTO.getStatus());
        contaExistente.setCategoria(contaReceberDTO.getCategoria());

        // Atualizar usuário se fornecido
        if (contaReceberDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(contaReceberDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            contaExistente.setUsuario(usuario);
        } else if (contaReceberDTO.getUsuarioDTO() != null && contaReceberDTO.getUsuarioDTO().getId() != null) {
            Usuario usuario = usuarioRepository.findById(contaReceberDTO.getUsuarioDTO().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            contaExistente.setUsuario(usuario);
        }
        // Se nem usuarioId nem usuarioDTO forem fornecidos, mantenha o usuário existente

        ContaReceber contaAtualizada = contaReceberRepository.save(contaExistente);
        return converterEntidadeParaDto(contaAtualizada);
    }


    private ContaReceber converterDtoParaEntidade(ContaReceberDTO contaReceberDTO) {
        if (contaReceberDTO == null) return null;

        ContaReceber contaReceber = new ContaReceber();
        contaReceber.setDescricao(contaReceberDTO.getDescricao());
        contaReceber.setValor(contaReceberDTO.getValor());
        contaReceber.setDataRecebimento(contaReceberDTO.getDataRecebimento());
        contaReceber.setStatus(contaReceberDTO.getStatus());
        contaReceber.setCategoria(contaReceberDTO.getCategoria());

        return contaReceber;
    }

    private ContaReceberDTO converterEntidadeParaDto(ContaReceber contaReceber) {
        if (contaReceber == null) return null;

        ContaReceberDTO dto = ContaReceberDTO.builder()
                .id(contaReceber.getId())
                .descricao(contaReceber.getDescricao())
                .valor(contaReceber.getValor())
                .dataRecebimento(contaReceber.getDataRecebimento())
                .status(contaReceber.getStatus())
                .categoria(contaReceber.getCategoria())
                .build();

        if (contaReceber.getUsuario() != null) {
            dto.setUsuarioId(contaReceber.getUsuario().getId());
            dto.setUsuarioDTO(UsuarioDTO.fromUsuario(contaReceber.getUsuario()));
        }

        return dto;
    }

        private ContaReceberDTO convertToDto (ContaReceber novaConta){
            return getContaReceberDTO(novaConta);
        }

        public static ContaReceberDTO getContaReceberDTO (ContaReceber novaConta){
            ContaReceberDTO novaContaDTO = new ContaReceberDTO();
            novaContaDTO.setId(novaConta.getId());
            novaContaDTO.setDescricao(novaConta.getDescricao());
            novaContaDTO.setValor(novaConta.getValor());
            novaContaDTO.setDataRecebimento(novaConta.getDataRecebimento());
            novaContaDTO.setStatus(novaConta.getStatus());
            novaContaDTO.setCategoria(novaConta.getCategoria());
            novaContaDTO.setUsuarioDTO(UsuarioDTO.fromUsuario(novaConta.getUsuario()));
            return novaContaDTO;
        }

    public boolean excluirContaReceber(Long id, Authentication authentication) {
        try {
            Long idUsuario = obterIdUsuario(authentication);

            Optional<ContaReceber> contaReceber = contaReceberRepository.findByIdAndUsuarioId(id, idUsuario);
            if (contaReceber.isEmpty()) {
                throw new CartaoCreditoException("Conta não encontrada para exclusão.");
            }

            try {
                contaReceberRepository.deleteById(id);
                return true;
            } catch (DataIntegrityViolationException e) {
                throw new CartaoCreditoException("Erro ao excluir conta: Problema de integridade de dados.", e);
            }

        } catch (SecurityException ex) {
            throw new CartaoCreditoException("Erro de segurança ao excluir a conta: " + ex.getMessage(), ex);
        } catch (IllegalArgumentException e) {
            throw new CartaoCreditoException("Dados inválidos para a exclusão da conta: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao excluir a conta: " + e.getMessage(), e);
        }
    }

    private Long obterIdUsuario(Authentication authentication) {
        logger.debug("Tipo de authentication.getPrincipal(): {}", authentication.getPrincipal().getClass().getName());
        logger.debug("Conteúdo de authentication.getPrincipal(): {}", authentication.getPrincipal().toString());

        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return usuarioRepository.findByEmail(userDetails.getUsername())
                    .map(Usuario::getId) // Substitua 'User' por 'Usuario' ou o nome correto da sua classe
                    .orElseThrow(() -> new IllegalStateException("Usuário não encontrado."));
        } else if (authentication.getPrincipal() instanceof String username) {
            return usuarioRepository.findByEmail(username)
                    .map(Usuario::getId) // Substitua 'User' por 'Usuario' ou o nome correto da sua classe
                    .orElseThrow(() -> new IllegalStateException("Usuário não encontrado."));
        }

        throw new IllegalStateException("Tipo de usuário não suportado.");
    }

    static class ServiceException extends RuntimeException {
        public ServiceException(String message) {
            super(message);
        }
    }
}