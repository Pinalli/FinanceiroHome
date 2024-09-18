package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.ContaPagar;
import br.com.pinalli.financeirohome.repository.ContaPagarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaPagarService {

    @Autowired
    private ContaPagarRepository contaPagarRepository;

    public ContaPagar criarContaPagar(ContaPagar contaPagar) {
        // Adicione validações, lógica de negócio, etc. aqui
        return contaPagarRepository.save(contaPagar);
    }

    public List<ContaPagar> listarContasPagar() {
        return contaPagarRepository.findAll();
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