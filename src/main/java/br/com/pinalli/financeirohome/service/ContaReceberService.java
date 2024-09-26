package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.ContaReceberDTO;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import br.com.pinalli.financeirohome.exception.ContaReceberException;
import br.com.pinalli.financeirohome.model.ContaReceber;
import br.com.pinalli.financeirohome.model.Usuario; // Ajuste o caminho conforme necessário
import br.com.pinalli.financeirohome.repository.ContaReceberRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

        Usuario usuario = null;
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
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }

        Long idUsuario = obterIdUsuario(authentication);
        if (idUsuario == null) {
            throw new SecurityException("Falha ao obter o ID do usuário.");
        }

        ContaReceber conta = contaReceberRepository.findByIdAndUsuarioId(id, idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada ou não pertence ao usuário."));

        try {
            contaReceberRepository.delete(conta);
            logger.debug("Conta excluída com sucesso.");
            return true;
        } catch (DataAccessException ex) {
            logger.error("Erro ao excluir conta: Erro de acesso a dados", ex);
            throw new ContaReceberException("Erro ao excluir conta: Problema de acesso ao banco de dados", ex);
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

    /**   public ContaReceberDTO atualizarContaReceber (Long id, ContaReceberDTO contaReceberDTO){
            Optional<ContaReceber> contaReceberOptional = obterContaReceberPorId(id);
            if (contaReceberOptional.isPresent()) {
                ContaReceber contaReceber = contaReceberOptional.get();
                contaReceber.setDescricao(contaReceberDTO.getDescricao());
                contaReceber.setValor(contaReceberDTO.getValor());
                contaReceber.setDataRecebimento(contaReceberDTO.getDataRecebimento());
                contaReceber.setStatus(contaReceberDTO.getStatus());
                contaReceber.setCategoria(contaReceberDTO.getCategoria());
                contaReceber.setUsuario(usuarioRepository.findById(contaReceberDTO.getUsuarioDTO().getId())
                        .orElseThrow(() -> new UserServiceException("Usuário não encontrado")));
                ContaReceber updatedContaReceber = contaReceberRepository.save(contaReceber);
                return convertToDto(updatedContaReceber);
            } else {
                throw new ServiceException("Conta a receber não encontrada");
            }
        }
  public void deletarContaReceber (Long id){
            if (contaReceberRepository.existsById(id)) {
                contaReceberRepository.deleteById(id);
            }
        }
    }
      */


     class ServiceException extends RuntimeException {
        public ServiceException(String message) {
            super(message);
        }
    }
}