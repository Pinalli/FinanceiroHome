package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.ContaPagar;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.ContaPagarRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContaPagarService {

    @Autowired
    private ContaPagarRepository contaPagarRepository;
    @Autowired
    private UsuarioService userService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public ContaPagar criarContaPagar(ContaPagar contaPagar) {
        // Obter o email do usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Buscar o usuário no banco de dados usando o email
        Usuario usuario = usuarioRepository.findById(userService.getUsuarioAutenticado().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Definir o usuário na conta a pagar
        contaPagar.setUsuario(usuario);

        // Salvar a conta a pagar no banco de dados
        return contaPagarRepository.save(contaPagar);

    }

    public Optional<ContaPagar> obterContaPagarPorId(Long id) {
        return contaPagarRepository.findById(id);
    }

    public Optional<ContaPagar> atualizarContaPagar(Long id, ContaPagar contaPagarAtualizada) {
        return contaPagarRepository.findById(id)
                .map(contaExistente -> {
                    contaExistente.setDescricao(contaPagarAtualizada.getDescricao());
                    // Atualize outros campos conforme necessário
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