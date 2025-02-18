package br.com.pinalli.financeirohome.service;


import br.com.pinalli.financeirohome.dto.ContaRequest;
import br.com.pinalli.financeirohome.dto.ContaResponse;
import br.com.pinalli.financeirohome.exception.CategoriaInvalidaException;
import br.com.pinalli.financeirohome.model.*;
import br.com.pinalli.financeirohome.repository.ContaRepository;
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

    public Conta criarConta(ContaRequest request, Usuario usuario) {
        Categoria categoria = categoriaService.buscarPorIdEUsuario(request.categoriaId(), usuario);
        validarCompatibilidadeTipo(request.tipo(), categoria.getTipo());

        Conta conta = new Conta();
        conta.setDescricao(request.descricao());
        conta.setValor(request.valor());
        conta.setDataVencimento(request.dataVencimento());
        conta.setTipo(request.tipo());
        conta.setUsuario(usuario);

        conta.setCategoria(categoria);
        return contaRepository.save(conta);
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
                conta.getId(),
                conta.getDescricao(),
                conta.getValor(),
                conta.getDataVencimento(),
                conta.getDataPagamento(),
                conta.getTipo(),
                conta.getStatus(),
                conta.getCategoria().getId(),
                conta.getCategoria().getNome()
        );
    }
}