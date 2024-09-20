package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.ContaReceberDTO;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import br.com.pinalli.financeirohome.model.ContaReceber;
import br.com.pinalli.financeirohome.model.TipoNotificacao;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.ContaReceberRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaReceberService {

    @Setter
    @Autowired
    private ContaReceberRepository contaReceberRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    public ContaReceberDTO criarContaReceber(ContaReceberDTO contaReceberDTO) {
        if (contaReceberDTO.getDescricao() == null || contaReceberDTO.getDescricao().isEmpty()) {
            throw new IllegalArgumentException("A descrição da conta a receber é obrigatória.");
        }
        // Converter DTO para entidade
        ContaReceber novaConta = new ContaReceber();
        novaConta.setDescricao(contaReceberDTO.getDescricao());
        novaConta.setValor(contaReceberDTO.getValor());
        novaConta.setDataRecebimento(contaReceberDTO.getDataRecebimento());
        novaConta.setStatus(contaReceberDTO.getStatus());
        novaConta.setCategoria(contaReceberDTO.getCategoria());

        // Obter o usuário do DTO (assumindo que o DTO contém o ID do usuário)
        Usuario usuario = usuarioRepository.findById(contaReceberDTO.getUsuarioDTO().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Definir o usuário na conta
        novaConta.setUsuario(usuario);

        // Criar notificação
        notificacaoService.criarNotificacao(novaConta.getUsuario(),
                novaConta, TipoNotificacao.EMAIL);

        // Converter a entidade para DTO e retornar
        return convertToDto(novaConta);
    }

    private ContaReceberDTO convertToDto(ContaReceber novaConta) {
        ContaReceberDTO novaContaDTO = new ContaReceberDTO();
        novaContaDTO.setId(novaConta.getId());
        novaContaDTO.setDescricao(novaConta.getDescricao());
        novaContaDTO.setValor(novaConta.getValor());

        novaContaDTO.setDataRecebimento(novaConta.getDataRecebimento());
        novaContaDTO.setStatus(novaConta.getStatus());
        novaContaDTO.setCategoria(novaConta.getCategoria());
        novaContaDTO.setUsuarioDTO((UsuarioDTO.fromUsuario(novaConta.getUsuario())));
        return novaContaDTO;
    }

    public List<ContaReceberDTO> listarContasReceber() {
        // Busca todas as contas a receber do banco de dados através do repositório
        List<ContaReceber> contasReceber = contaReceberRepository.findAll();

        // Converte a lista de ContaReceber para uma lista de ContaReceberDTO
        return contasReceber.stream() // Inicia o fluxo de dados (stream) a partir da lista de contas a receber
                .map(this::convertToDto) // Aplica o método 'convertToDto' para cada item da lista, transformando de ContaReceber para ContaReceberDTO
                .collect(Collectors.toList()); // Coleta o resultado e converte de volta para uma lista de ContaReceberDTO
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