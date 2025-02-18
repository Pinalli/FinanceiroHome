package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import br.com.pinalli.financeirohome.exception.UsuarioNaoAutenticadoException;
import br.com.pinalli.financeirohome.exception.UsuarioNaoEncontradoException;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Converte a entidade Usuario para UsuarioDTO
    public UsuarioDTO converterParaDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public void cadastrarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já está em uso");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuarioDTO.getNome());
        novoUsuario.setEmail(usuarioDTO.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        usuarioRepository.save(novoUsuario);
        logger.info("Novo usuário cadastrado: {}", novoUsuario.getEmail());
    }


    public Long obterIdPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
        return usuario.getId();
    }

    public UsuarioDTO buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            return converterParaDTO(usuario); // Retorna o DTO
        }
        throw new EntityNotFoundException("Usuário não encontrado.");
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioAutenticado() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Verifica se há autenticação
            if (authentication == null) {
                logger.error("Não há usuário autenticado no contexto.");
                throw new UsuarioNaoAutenticadoException("Não há usuário autenticado no contexto");
            }
            logger.info("Autenticação encontrada para o usuário. Principal: {}", authentication.getPrincipal());

            // Verifica se o principal é do tipo UserDetails
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof UserDetails)) {
                logger.error("Principal não é uma instância de UserDetails.");
                throw new UsuarioNaoAutenticadoException("Principal não é uma instância de UserDetails");
            }
            logger.info("Principal é uma instância de UserDetails.");

            // Recupera o email do principal
            String email = ((UserDetails) principal).getUsername();
            logger.info("Email do usuário autenticado: {}", email);

            // Busca o usuário pelo email
            return usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.error("Usuário não encontrado com email: {}", email);
                        return new UsuarioNaoEncontradoException("Usuário não encontrado com email: " + email);
                    });

        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException e) {
            logger.error("Erro ao recuperar o usuário autenticado: {}", e.getMessage());
            throw e; // Re-throw the exception
        } catch (Exception e) {
            logger.error("Erro inesperado ao recuperar o usuário autenticado: {}", e.getMessage(), e);
            throw new RuntimeException("Erro inesperado ao recuperar o usuário autenticado", e);
        }
    }

    public Usuario atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        }
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent() && !usuario.getEmail().equals(usuarioDTO.getEmail())) {
            throw new RuntimeException("E-mail já está em uso");
        }

        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());

        return usuarioRepository.save(usuario);
    }

    public void excluirUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o email: " + email));
    }
}