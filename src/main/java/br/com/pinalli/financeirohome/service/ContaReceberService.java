package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.ContaReceberDTO;
import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import br.com.pinalli.financeirohome.model.ContaReceber;
import br.com.pinalli.financeirohome.model.StatusConta;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.ContaReceberRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import lombok.AllArgsConstructor;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ContaReceberService {

    private ContaReceberRepository contaReceberRepository;
    private UsuarioRepository usuarioRepository;


    public ContaReceberDTO criarContaReceber(ContaReceberDTO contaReceberDTO) {
        if (contaReceberDTO == null || contaReceberDTO.getDescricao() == null || contaReceberDTO.getDescricao().isEmpty()) {
            throw new IllegalArgumentException("Dados inválidos para a conta a receber.");
        }

        Usuario usuario = null;
        if (contaReceberDTO.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(contaReceberDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        } else if (contaReceberDTO.getUsuarioDTO() != null && contaReceberDTO.getUsuarioDTO().getId() != null) {
            usuario = usuarioRepository.findById(contaReceberDTO.getUsuarioDTO().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        } else {
            throw new IllegalArgumentException("Informações do usuário não fornecidas.");
        }

        ContaReceber novaConta = converterDtoParaEntidade(contaReceberDTO);
        novaConta.setUsuario(usuario);

        ContaReceber contaSalva = contaReceberRepository.save(novaConta);
        return converterEntidadeParaDto(contaSalva);
    }

    private ContaReceber converterDtoParaEntidade(ContaReceberDTO contaReceberDTO) {
        if (contaReceberDTO == null) return null;

        ContaReceber contaReceber = new ContaReceber();
        contaReceber.setDescricao(contaReceberDTO.getDescricao());
        contaReceber.setValor(contaReceberDTO.getValor());
        contaReceber.setDataRecebimento(contaReceberDTO.getDataRecebimento());
        contaReceber.setStatus(contaReceberDTO.getStatus());
        contaReceber.setCategoria(contaReceberDTO.getCategoria());

        return contaReceber;
    }

    private ContaReceberDTO converterEntidadeParaDto(ContaReceber contaReceber) {
        if (contaReceber == null) return null;

        ContaReceberDTO dto = ContaReceberDTO.builder()
                .id(contaReceber.getId())
                .descricao(contaReceber.getDescricao())
                .valor(contaReceber.getValor())
                .dataRecebimento(contaReceber.getDataRecebimento())
                .status(contaReceber.getStatus())
                .categoria(contaReceber.getCategoria())
                .build();

        if (contaReceber.getUsuario() != null) {
            dto.setUsuarioId(contaReceber.getUsuario().getId());
            dto.setUsuarioDTO(UsuarioDTO.fromUsuario(contaReceber.getUsuario()));
        }

        return dto;
    }


    private UsuarioDTO converterUsuarioParaDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(Objects.requireNonNullElse(usuario.getId(),0L))
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }

        private ContaReceberDTO convertToDto (ContaReceber novaConta){
            return getContaReceberDTO(novaConta);
        }

        public static ContaReceberDTO getContaReceberDTO (ContaReceber novaConta){
            ContaReceberDTO novaContaDTO = new ContaReceberDTO();
            novaContaDTO.setId(novaConta.getId());
            novaContaDTO.setDescricao(novaConta.getDescricao());
            novaContaDTO.setValor(novaConta.getValor());
            novaContaDTO.setDataRecebimento(novaConta.getDataRecebimento());
            novaContaDTO.setStatus(novaConta.getStatus());
            novaContaDTO.setCategoria(novaConta.getCategoria());
            novaContaDTO.setUsuarioDTO(UsuarioDTO.fromUsuario(novaConta.getUsuario()));
            return novaContaDTO;
        }

        public List<ContaReceberDTO> listarContasReceber () {
            // Busca todas as contas a receber do banco de dados através do repositório
            List<ContaReceber> contasReceber = contaReceberRepository.findAll();

            // Converte a lista de ContaReceber para uma lista de ContaReceberDTO
            return contasReceber.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
/**
    public List<ContaReceberDTO> listarContasReceber() {
        return contaReceberRepository.findAll().stream()
                .map(this::converterEntidadeParaDto)
                .collect(Collectors.toList());
    }*/

        public ContaReceberDTO buscarContaReceber (Long id){
            Optional<ContaReceber> contaReceberOptional = obterContaReceberPorId(id);
            return contaReceberOptional.map(this::convertToDto).orElseThrow(() -> new ServiceException("Conta a receber não encontrada"));
        }

        public Optional<ContaReceber> obterContaReceberPorId (Long id){
            return contaReceberRepository.findById(id);
        }


        public ContaReceberDTO atualizarContaReceber (Long id, ContaReceberDTO contaReceberDTO){
            Optional<ContaReceber> contaReceberOptional = obterContaReceberPorId(id);
            if (contaReceberOptional.isPresent()) {
                ContaReceber contaReceber = contaReceberOptional.get();
                contaReceber.setDescricao(contaReceberDTO.getDescricao());
                contaReceber.setValor(contaReceberDTO.getValor());
                contaReceber.setDataRecebimento(contaReceberDTO.getDataRecebimento());
                contaReceber.setStatus(contaReceberDTO.getStatus());
                contaReceber.setCategoria(contaReceberDTO.getCategoria());
                contaReceber.setUsuario(usuarioRepository.findById(contaReceberDTO.getUsuarioDTO().getId())
                        .orElseThrow(() -> new UserServiceException("Usuário não encontrado")));
                ContaReceber updatedContaReceber = contaReceberRepository.save(contaReceber);
                return convertToDto(updatedContaReceber);
            } else {
                throw new ServiceException("Conta a receber não encontrada");
            }
        }

        public void deletarContaReceber (Long id){
            if (contaReceberRepository.existsById(id)) {
                contaReceberRepository.deleteById(id);
            }
        }
    }

    // Custom exception classes
    class UserServiceException extends RuntimeException {
        public UserServiceException(String message) {
            super(message);
        }
    }

    class ServiceException extends RuntimeException {
        public ServiceException(String message) {
            super(message);
        }
    }