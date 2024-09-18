package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.ContaReceber;
import br.com.pinalli.financeirohome.repository.ContaReceberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaReceberService {

    @Autowired
    private ContaReceberRepository contaReceberRepository;

    public ContaReceber criarContaReceber(ContaReceber contaReceber) {
        // Adicione validações, lógica de negócio, etc. aqui
        return contaReceberRepository.save(contaReceber);
    }

    public List<ContaReceber> listarContasReceber() {
        return contaReceberRepository.findAll();
    }

    // Adicione outros métodos para atualizar, excluir, etc. conforme necessário
}