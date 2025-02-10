package br.com.pinalli.financeirohome.service;


import br.com.pinalli.financeirohome.dto.ContaDTO;
import br.com.pinalli.financeirohome.model.Conta;
import br.com.pinalli.financeirohome.repository.ContaRepository;
import org.springframework.stereotype.Service;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Conta criarConta(ContaDTO contaDTO) { // Nome do método corrigido
        Conta conta = new Conta();
        conta.setDescricao(contaDTO.getDescricao()); // Usando a instância "contaDTO"
        conta.setTipo(contaDTO.getTipo()); // Método corrigido (assumindo que existe "getTipo()")
        // ... outros campos (ex: valor, data, status)
        return contaRepository.save(conta); // Instância "conta" passada para save()
    }
}