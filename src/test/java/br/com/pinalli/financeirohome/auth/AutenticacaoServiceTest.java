package br.com.pinalli.financeirohome.auth;

import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AutenticacaoServiceTest autenticacaoService;


    @Test
    public void testFindByEmail_EmailEncontrado() {
        // Criar um usuário fake
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@example.com");
        usuario.setSenha("senha123");

        // Mockar o repositório para retornar o usuário fake
        when(usuarioRepository.findByEmail("usuario@example.com")).thenReturn(Optional.of(usuario));

        // Chamar o método findByEmail
        Usuario result = autenticacaoService.findByEmail("usuario@example.com");

        // Verificar se o usuário foi encontrado
        assertNotNull(result);
        assertEquals("usuario@example.com", result.getEmail());
    }

    private Usuario findByEmail(String email) {
        if ("usuario@exemplo.com".equals(email)) {
            return new Usuario("Usuario Teste", email, "senha123");
        }
        return null;
    }

    @Test
    public void testFindByEmail_EmailNaoEncontrado() {
        // Mockar o repositório para não encontrar o usuário
        when(usuarioRepository.findByEmail("usuario@example.com")).thenReturn(Optional.empty());

        // Chamar o método findByEmail
        assertThrows(UsernameNotFoundException.class, () -> autenticacaoService.findByEmail("usuario@example.com"));
    }

    @Test
    public void testLoadUserByUsername_EmailEncontrado() {
        // Criar um usuário fake
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@example.com");
        usuario.setSenha("senha123");

        // Mockar o repositório para retornar o usuário fake
        when(usuarioRepository.findByEmail("usuario@example.com")).thenReturn(Optional.of(usuario));

        // Chamar o método loadUserByUsername
        UserDetails result = autenticacaoService.loadUserByUsername("usuario@example.com");

        // Verificar se o usuário foi encontrado
        assertNotNull(result);
        assertEquals("usuario@example.com", result.getUsername());
    }

    private UserDetails loadUserByUsername(String mail) {

        if ("usuario@exemplo.com".equals(mail)) {
            return new org.springframework.security.core.userdetails.User("Usuario Teste", "senha123", true, true, true, true, null);
        }
        return null;

    }

    @Test
    public void testLoadUserByUsername_EmailNaoEncontrado() {
        // Mockar o repositório para não encontrar o usuário
        when(usuarioRepository.findByEmail("usuario@example.com")).thenReturn(Optional.empty());

        // Chamar o método loadUserByUsername
        assertThrows(UsernameNotFoundException.class, () -> autenticacaoService.loadUserByUsername("usuario@example.com"));
    }
}