package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.exception.CartaoNaoEncontradoException;
import br.com.pinalli.financeirohome.exception.CompraNaoEncontradaException;
import br.com.pinalli.financeirohome.exception.LimiteInsuficienteException;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.Compra;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
public class CartaoCreditoService {

    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;
    @Autowired
    private CompraRepository  compraRepository;

    public CartaoCreditoService(CartaoCreditoRepository cartaoCreditoRepository, CompraRepository compraRepository) {
        this.cartaoCreditoRepository = cartaoCreditoRepository;
        this.compraRepository = compraRepository;
    }

    public CartaoCredito criarCartaoCredito(CartaoCredito cartaoCredito) {
        Objects.requireNonNull(cartaoCredito, "O cartão de crédito não pode ser nulo.");
        if (cartaoCredito.getLimite().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O limite do cartão deve ser maior que zero.");
        }
        return cartaoCreditoRepository.save(cartaoCredito);
    }

    public List<CartaoCredito> listarCartoesPorUsuario(Long usuarioId) {
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");
        return cartaoCreditoRepository.findByUsuarioId(usuarioId);
    }

    public CartaoCredito atualizarCartaoCredito(CartaoCredito cartaoCredito) {
        Objects.requireNonNull(cartaoCredito, "O cartão de crédito não pode ser nulo.");
        return cartaoCreditoRepository.save(cartaoCredito);
    }

    public void deletarCartaoCredito(Long id) {
        Objects.requireNonNull(id, "O ID do cartão não pode ser nulo.");
        if (!cartaoCreditoRepository.existsById(id)) {
            throw new CartaoNaoEncontradoException("O cartão de crédito com o ID fornecido não existe.");
        }
        cartaoCreditoRepository.deleteById(id);
    }

    public BigDecimal calcularLimiteDisponivel(Long cartaoId) {
        Objects.requireNonNull(cartaoId, "O ID do cartão não pode ser nulo.");
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão não encontrado com o ID fornecido."));

        return cartao.getLimite().subtract(cartao.getValor());
    }

    public Compra registrarCompra(Compra compra) {
        Objects.requireNonNull(compra, "A compra não pode ser nula.");
        Objects.requireNonNull(compra.getCartaoCredito(), "O cartão de crédito na compra não pode ser nulo.");

        CartaoCredito cartao = cartaoCreditoRepository.findById(compra.getCartaoCredito().getId())
                .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão de crédito não encontrado para a compra."));

        BigDecimal limiteDisponivel = cartao.getLimite().subtract(cartao.getValor());
        if (compra.getValor().compareTo(limiteDisponivel) > 0) {
            throw new LimiteInsuficienteException("Limite insuficiente para realizar a compra.");
        }
        cartao.setValor(cartao.getValor().add(compra.getValor()));
        cartaoCreditoRepository.save(cartao);

        return compraRepository.save(compra);
    }


    public List<Compra> listarComprasPorCartao(Long cartaoId) {
        Objects.requireNonNull(cartaoId, "O ID do cartão não pode ser nulo.");
        return compraRepository.findByCartaoCreditoId(cartaoId);
    }

    public void pagarParcela(Long compraId) {
        Objects.requireNonNull(compraId, "O ID da compra não pode ser nulo.");
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new CompraNaoEncontradaException("Compra não encontrada com o ID fornecido."));

        if (compra.getParcelasPagas() >= compra.getParcelas()) {
            throw new IllegalArgumentException("Todas as parcelas já foram pagas.");
        }

        BigDecimal valorParcela = compra.getValor().divide(BigDecimal.valueOf(compra.getParcelas()), 2, RoundingMode.HALF_UP);

        CartaoCredito cartao = compra.getCartaoCredito();
        cartao.setValor(cartao.getValor().subtract(valorParcela));
        compra.setParcelasPagas(compra.getParcelasPagas() + 1);

        compraRepository.save(compra);
        cartaoCreditoRepository.save(cartao);
    }


    public BigDecimal calcularTotalComprasEmAberto(Long cartaoId) {
        Objects.requireNonNull(cartaoId, "O ID do cartão não pode ser nulo.");
        List<Compra> compras = compraRepository.findByCartaoCreditoId(cartaoId);

        return compras.stream()
                .map(compra -> {
                    BigDecimal valorParcela = compra.getValor().divide(BigDecimal.valueOf(compra.getParcelas()), 2, RoundingMode.HALF_UP);
                    int parcelasRestantes = compra.getParcelas() - compra.getParcelasPagas();
                    return valorParcela.multiply(BigDecimal.valueOf(parcelasRestantes));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
