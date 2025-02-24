package br.com.pinalli.financeirohome.service;


import br.com.pinalli.financeirohome.dto.ContaRequest;
import br.com.pinalli.financeirohome.dto.ContaResponse;
import br.com.pinalli.financeirohome.exception.CategoriaInvalidaException;
import br.com.pinalli.financeirohome.model.*;
import br.com.pinalli.financeirohome.repository.CategoriaRepository;
import br.com.pinalli.financeirohome.repository.ContaRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContaService {

    private final ContaRepository contaRepository;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public ContaResponse criarConta(ContaRequest request, Usuario usuario) {
        // Busca a categoria e o usuário
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
         usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se o tipo da categoria corresponde ao tipo da conta
        validarTipoCategoria(request.tipo(), String.valueOf(categoria.getTipo()));

        // Cria a conta
        Conta conta = new Conta();
        conta.setDescricao(request.descricao());
        conta.setValor(request.valor());
        conta.setDataVencimento(request.dataVencimento());
        conta.setDataPagamento(request.dataPagamento());
        conta.setStatus(StatusConta.valueOf(request.status()));
        conta.setTipo(TipoConta.valueOf(request.tipo()));
        conta.setObservacao(request.observacao());
        conta.setUsuario(usuario);
        conta.setCategoria(categoria);

        // Salva a conta
        Conta contaSalva = contaRepository.save(conta);

        // Converte a entidade Conta em ContaResponse
        return ContaResponse.fromConta(contaSalva);
    }

    // Método auxiliar para validar o tipo da categoria
    private void validarTipoCategoria(String tipoConta, String tipoCategoria) {
        if (("PAGAR".equals(tipoConta) && !"DESPESA".equals(tipoCategoria))) {
            throw new RuntimeException("A categoria deve ser do tipo DESPESA para contas a pagar");
        }
        if (("RECEBER".equals(tipoConta) && !"RECEITA".equals(tipoCategoria))) {
            throw new RuntimeException("A categoria deve ser do tipo RECEITA para contas a receber");
        }
    }


    public List<ContaResponse> listarContas() {
        return contaRepository.findAll().stream()
                .map(ContaResponse::fromConta) // Converte cada Conta em ContaResponse
                .toList();
    }

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
                conta.getId(), // id
                conta.getDescricao(), // descricao
                conta.getValor(), // valor
                conta.getTipo(), // tipo (TipoConta)
                conta.getDataVencimento(), // dataVencimento
                conta.getStatus(), // status (StatusConta)
                conta.getDataPagamento(), // dataPagamento
                conta.getObservacao(), // observacao
                conta.getUsuario().getId(), // usuarioId
                conta.getUsuario().getNome(), // usuarioNome
                conta.getCategoria().getId(), // categoriaId
                conta.getCategoria().getNome() // categoriaNome
        );
    }
}