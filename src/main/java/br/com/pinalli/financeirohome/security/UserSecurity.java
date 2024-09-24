package br.com.pinalli.financeirohome.security;

import br.com.pinalli.financeirohome.model.ContaPagar;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.ContaPagarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {

    @Autowired
    private ContaPagarRepository contaPagarRepository;

    public boolean checkId(Authentication authentication, Long contaPagarId) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        ContaPagar contaPagar = contaPagarRepository.findById(contaPagarId)
                .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada"));
        return usuarioLogado.getId().equals(contaPagar.getUsuario().getId());
    }
}