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
    public Conta criarConta(ContaDTO contaDTO) {
        Conta conta = new Conta();
        conta.setDescricao(contaDTO.getDescricao());
        conta.setTipo(contaDTO.isTipo()); // Define se é conta a pagar ou receber
        // ... outros campos
        return contaRepository.save(conta);
    }
}
