package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.ContaReceber;
import br.com.pinalli.financeirohome.model.TipoNotificacao;
import br.com.pinalli.financeirohome.repository.ContaReceberRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaReceberService {

    @Setter
    @Autowired
    private ContaReceberRepository contaReceberRepository;

    @Autowired // Injetar o NotificacaoService
    private NotificacaoService notificacaoService;

    public ContaReceber criarContaReceber(ContaReceber contaReceber) {
        if (contaReceber.getDescricao() == null || contaReceber.getDescricao().isEmpty()) {
            throw new IllegalArgumentException("A descrição da conta a receber é obrigatória.");
        }

        ContaReceber novaConta = contaReceberRepository.save(contaReceber); // Salvar apenas uma vez

        notificacaoService.criarNotificacao(novaConta.getUsuario(), novaConta, TipoNotificacao.EMAIL);

        return novaConta;
    }

    public List<ContaReceber> listarContasReceber() {
        return contaReceberRepository.findAll();
    }

    public Optional<ContaReceber> obterContaReceberPorId(Long id) {
        return contaReceberRepository.findById(id);
    }

    public Optional<ContaReceber> atualizarContaReceber(Long id, ContaReceber contaReceberAtualizada) {
        return contaReceberRepository.findById(id)
                .map(contaExistente -> {
                    contaExistente.setDescricao(contaReceberAtualizada.getDescricao());
                    // Atualize outros campos conforme necessário
                    return contaReceberRepository.save(contaExistente);
                });
    }

    public boolean excluirContaReceber(Long id) {
        if (contaReceberRepository.existsById(id)) {
            contaReceberRepository.deleteById(id);
            return true;
        }
        return false;
    }
}