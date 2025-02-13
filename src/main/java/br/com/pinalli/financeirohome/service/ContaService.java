package br.com.pinalli.financeirohome.service;


import br.com.pinalli.financeirohome.dto.ContaDTO;
import br.com.pinalli.financeirohome.model.*;
import br.com.pinalli.financeirohome.repository.CategoriaRepository;
import br.com.pinalli.financeirohome.repository.ContaRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public ContaService(ContaRepository contaRepository, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Conta criarConta(ContaDTO contaDTO) {
        Conta conta = new Conta();
        conta.setDescricao(contaDTO.getDescricao());
        conta.setValor(contaDTO.getValor());
        conta.setDataVencimento(contaDTO.getDataVencimento());
        conta.setTipo(contaDTO.getTipo());
        conta.setStatus(contaDTO.getStatus() != null ? contaDTO.getStatus() : StatusConta.PENDENTE);
        conta.setRecorrente(contaDTO.isRecorrente());
        conta.setPeriodicidade(contaDTO.getPeriodicidade());
        conta.setObservacao(contaDTO.getObservacao());

        Usuario usuario = usuarioRepository.findById(contaDTO.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        conta.setUsuario(usuario);

        if (contaDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(contaDTO.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
            conta.setCategoria(categoria);
        }

        return contaRepository.save(conta);
    }

    public List<Conta> listarContasPorTipo(String tipo) {
        return contaRepository.findByTipo(TipoConta.valueOf(tipo)); // Query com filtro
    }



}
