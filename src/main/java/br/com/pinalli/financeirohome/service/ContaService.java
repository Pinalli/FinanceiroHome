package br.com.pinalli.financeirohome.service;


import br.com.pinalli.financeirohome.dto.ContaRequest;
import br.com.pinalli.financeirohome.dto.ContaResponse;
import br.com.pinalli.financeirohome.exception.CategoriaInvalidaException;
import br.com.pinalli.financeirohome.model.*;
import br.com.pinalli.financeirohome.repository.CategoriaRepository;
import br.com.pinalli.financeirohome.repository.ContaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContaService {

    private final ContaRepository contaRepository;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;
    private final CategoriaRepository categoriaRepository;

    public ContaResponse criarConta(ContaRequest request, Usuario usuario) {

        // Buscar categoria no banco
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        Conta conta = new Conta();
        // Preencha os campos da conta com base no request e usuário
        // Exemplo:
        conta.setDescricao(request.descricao());
        conta.setValor(request.valor());
        conta.setDataVencimento(request.dataVencimento());
        conta.setTipo(request.tipo());
        conta.setStatus(StatusConta.PENDENTE);
        conta.setCategoria(categoria);
        conta.setUsuario(usuario); // Associar ao usuário autenticado

        // Salve a conta no banco de dados
        Conta savedConta = contaRepository.save(conta);

        // Retornar resposta
        return new ContaResponse(
                savedConta.getId(),
                savedConta.getDescricao(),
                savedConta.getValor(),
                savedConta.getTipo(),
                savedConta.getDataVencimento(),
                savedConta.getStatus(),
                savedConta.getCategoria().getNome() // Pegando nome da categoria
        );
    }
        // Converta a entidade para DTO
      //  return convertToResponse(savedConta);

    public List<ContaResponse> listarContasPorTipo(TipoConta tipo, Usuario usuario) {
        return contaRepository.findByTipoAndUsuario(tipo, usuario)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private void validarCompatibilidadeTipo(TipoConta tipoConta, TipoCategoria tipoCategoria) {
        if ((tipoConta == TipoConta.PAGAR && tipoCategoria != TipoCategoria.DESPESA) ||
                (tipoConta == TipoConta.RECEBER && tipoCategoria != TipoCategoria.RECEITA)) {
            throw new CategoriaInvalidaException("Categoria incompatível com o tipo de conta");
        }
    }

    private ContaResponse convertToResponse(Conta conta) {
        return new ContaResponse(
                conta.getId(),
                conta.getDescricao(),
                conta.getValor(),
                conta.getTipo(),
                conta.getDataVencimento(),
                conta.getStatus(),
                conta.getCategoria().getNome() // Supondo que Conta tenha uma relação com Categoria
        );
    }
}