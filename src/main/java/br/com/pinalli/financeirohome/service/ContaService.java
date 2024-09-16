package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.Conta;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public void criarConta(String descricao, double valor, boolean tipo, int usuarioId) {
        // criar uma nova conta no banco de dados
        Conta conta = new Conta();
        conta.setDescricao(descricao);
        conta.setValor(valor);
        conta.setTipo(tipo);

        // criar um objeto Usuario com o id correspondente
        Usuario usuario = new Usuario();
        usuario.setId((long) usuarioId);

        // setar o objeto Usuario na conta
        conta.setUsuario(usuario);

        // inserir a conta no banco de dados
        // ...
    }
    public void registarContaPagar(String descricao, double valor, Date dataVencimento, int usuarioId) {
        // ...
    }

    public List<Conta> listarContasPagar(int usuarioId) {
        return contaRepository.findByUsuarioIdAndTipo((long) usuarioId, false);
    }
}

