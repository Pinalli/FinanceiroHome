package br.com.pinalli.financeirohome.service;// ContaPagarService.java

import br.com.pinalli.financeirohome.dto.ContaPagarDTO;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import br.com.pinalli.financeirohome.model.ContaPagar;
import br.com.pinalli.financeirohome.model.StatusConta;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import br.com.pinalli.financeirohome.repository.ContaPagarRepository;
import org.springframework.security.core.Authentication;
import br.com.pinalli.financeirohome.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaPagarService {

    private static final Logger logger = LoggerFactory.getLogger(ContaPagarService.class);

    private final ContaPagarRepository contaPagarRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ContaPagarService(ContaPagarRepository contaPagarRepository, UsuarioRepository usuarioRepository) {
        this.contaPagarRepository = contaPagarRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ContaPagar criarContaPagar(ContaPagar contaPagar) {
        if (contaPagar == null) {
            throw new IllegalArgumentException("Conta a pagar não pode ser nula");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }

        String emailUsuario = authentication.getName();
        Usuario usuarioLogado = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));

        contaPagar.setUsuario(usuarioLogado);
        return contaPagarRepository.save(contaPagar);
    }

    @PreAuthorize("hasRole('USER')")
    public List<ContaPagarDTO> listarContasPagarDoUsuario(Authentication authentication) {
        Long idUsuario = obterIdUsuario(authentication);
        List<ContaPagar> contas = contaPagarRepository.findByUsuarioId(idUsuario);
        if(contas == null) throw new IllegalArgumentException("Nenhuma conta encontrada para o usuário.");


        return converterContasParaDTOs(contas);
    }
/**
    @PreAuthorize("hasRole('USER')") //IMPORTANTE: validação de permissão para acesso.
    public List<ContaPagarDTO> listarContasPagarDoUsuario(Authentication authentication) {
        logger.info("Iniciando listagem de contas a pagar para o usuário");
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Tentativa de acesso não autenticado");
            throw new SecurityException("Usuário não autenticado.");
        }

        Long idUsuario;
        try {
            idUsuario = obterIdUsuario(authentication);

            if (idUsuario == null) {
                throw new SecurityException("Erro ao obter o ID do usuário.");
            }

            List<ContaPagar> contasPagar = contaPagarRepository.findByUsuarioId(idUsuario);
            return converterContasParaDTOs(contasPagar);


        } catch (UsernameNotFoundException e) {
            throw new SecurityException("Usuário não encontrado: " + e.getMessage(), e);
        } catch (IllegalArgumentException ex) {
            throw new SecurityException("Erro ao processar a requisição: " + ex.getMessage(), ex);
        } catch (Exception e) {
            throw new SecurityException("Erro inesperado ao listar as contas: " + e.getMessage(), e); // Detalhes do erro
        }
    }
*/


    //Crucial: obter o idUsuario do usuário autenticado
    private Long obterIdUsuario(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        } else if (principal instanceof String) {
            String email = (String) principal;
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            return usuario.getId();
        }
        throw new IllegalStateException("Tipo de usuário não suportado: " + principal.getClass().getName());
    }

    private UsuarioDTO converterUsuarioParaDTO(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
    //private UsuarioDTO converterUsuarioParaDTO(Usuario usuario) {
        //return UsuarioDTO.fromUsuario(usuario);
   // }

    private List<ContaPagarDTO> converterContasParaDTOs(List<ContaPagar> contas) {
        if (contas == null) {
            return List.of(); // Retorna uma lista vazia para evitar problemas com null
        }
        return contas.stream()
                .filter(Objects::nonNull)
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public ContaPagar converterDtoParaEntidade(ContaPagarDTO contaPagarDTO) {
        if (contaPagarDTO == null) {
            return null;
        }
        //Validações - Erros de dados
        if (contaPagarDTO.getValor() == null || contaPagarDTO.getDataVencimento() == null ||
                contaPagarDTO.getDescricao() == null || contaPagarDTO.getUsuario() == null ||
                contaPagarDTO.getUsuario().getId() == null || contaPagarDTO.getCategoria() == null || contaPagarDTO.getCategoria().isEmpty()) {
            throw new IllegalArgumentException("Dados inválidos para a conta a pagar.");
        }

        Usuario usuario = usuarioRepository.findById(contaPagarDTO.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        return ContaPagar.builder()
                .descricao(contaPagarDTO.getDescricao())
                .valor(contaPagarDTO.getValor())
                .dataVencimento(contaPagarDTO.getDataVencimento())
                .status(StatusConta.valueOf(contaPagarDTO.getStatus()))
                .categoria(contaPagarDTO.getCategoria())
                .usuario(usuario) // Correctly sets the Usuario object
                .build();
    }

    public ContaPagarDTO converterParaDTO(ContaPagar contaPagar) {
        if (contaPagar == null) return null;

        return ContaPagarDTO.builder()
                .id(contaPagar.getId())
                .descricao(contaPagar.getDescricao())
                .valor(contaPagar.getValor())
                .status(contaPagar.getStatus().name())
                .categoria(contaPagar.getCategoria())
                .usuario(converterUsuarioParaDTO(contaPagar.getUsuario()))
                .build();
    }


    public Optional<ContaPagar> obterContaPagarPorId(Long id) {
        if(id == null || id <= 0) throw new IllegalArgumentException("ID inválido para a conta a pagar.");
        return contaPagarRepository.findById(id);
    }


    public Optional<ContaPagar> atualizarContaPagar(Long id, ContaPagar contaPagarAtualizada) {
        //Verificação de existencia
        if (contaPagarAtualizada == null) throw new IllegalArgumentException("Conta a pagar inválida.");

        if (id == null || id <= 0) throw new IllegalArgumentException("ID inválido.");
        // Busca a conta a pagar pelo ID
        return contaPagarRepository.findById(id)
                .map(contaExistente -> {
                    // Atualiza os campos da conta a pagar
                    contaExistente.setDescricao(contaPagarAtualizada.getDescricao());
                    contaExistente.setValor(contaPagarAtualizada.getValor());
                    contaExistente.setDataVencimento(contaPagarAtualizada.getDataVencimento());
                    contaExistente.setStatus(contaPagarAtualizada.getStatus());
                    contaExistente.setCategoria(contaPagarAtualizada.getCategoria());
                    contaExistente.setUsuario(contaPagarAtualizada.getUsuario()); // Ajuste importante

                    // Salva a conta atualizada
                    return contaPagarRepository.save(contaExistente);
                });
    }


    public boolean excluirContaPagar(Long id) {
        if (contaPagarRepository.existsById(id)) {
            contaPagarRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }


}