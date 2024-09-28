package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Converte a entidade Usuario para UsuarioDTO
    public UsuarioDTO converterParaDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public void cadastrarUsuario(UsuarioDTO usuarioDTO, String senha) {
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já está em uso");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuarioDTO.getNome());
        novoUsuario.setEmail(usuarioDTO.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(senha));

        usuarioRepository.save(novoUsuario);
        logger.info("Novo usuário cadastrado: {}", novoUsuario.getEmail());
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


    public Usuario  getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String email = ((UserDetails) authentication.getPrincipal()).getUsername(); // Obter o email do UserDetails
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

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
}