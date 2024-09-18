package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.ContaPagar;
import br.com.pinalli.financeirohome.repository.ContaPagarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Adicione outros métodos para atualizar, excluir, etc. conforme necessário
}