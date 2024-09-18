package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(username);
    if (usuarioOptional.isPresent()) {
        Usuario usuario = usuarioOptional.get();
        // Mapeie os detalhes do usuário para um objeto UserDetails
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha()) // Certifique-se de que a senha esteja codificada corretamente (usando o PasswordEncoder)
                .authorities("ROLE_USER") // Defina as autorizações (papéis) do usuário
                .build();
    } else {
        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}}