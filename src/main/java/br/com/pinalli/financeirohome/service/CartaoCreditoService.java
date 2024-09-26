package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.Compra;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartaoCreditoService {

    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;
    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;



    public CartaoCredito criarCartaoCredito(CartaoCredito cartaoCredito) {
        return cartaoCreditoRepository.save(cartaoCredito);
    }

    public List<CartaoCredito> listarCartoesPorUsuario(Long usuarioId) {
        return cartaoCreditoRepository.findByUsuarioId(usuarioId);
    }

    public CartaoCredito atualizarCartaoCredito(CartaoCredito cartaoCredito) {
        return cartaoCreditoRepository.save(cartaoCredito);
    }

    public void deletarCartaoCredito(Long id) {
        cartaoCreditoRepository.deleteById(id);
    }

    public BigDecimal calcularLimiteDisponivel(Long cartaoId) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));
        return cartao.getLimite().subtract(cartao.getValor());
    }
    public Compra registrarCompra(Compra compra) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(compra.getCartaoCredito().getId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        BigDecimal limiteDisponivel = cartao.getLimite().subtract(cartao.getValor());
        if (compra.getValor().compareTo(limiteDisponivel) > 0) {
            throw new RuntimeException("Limite insuficiente para realizar a compra");
        }

        cartao.setValor(cartao.getValor().add(compra.getValor()));
        cartaoCreditoRepository.save(cartao);

        return compraRepository.save(compra);
    }

    public List<Compra> listarComprasPorCartao(Long cartaoId) {
        return compraRepository.findByCartaoCreditoId(cartaoId);
    }

    public void pagarParcela(Long compraId) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra não encontrada"));

        if (compra.getParcelasPagas() < compra.getParcelas()) {
            compra.setParcelasPagas(compra.getParcelasPagas() + 1);
            BigDecimal valorParcela = compra.getValor().divide(BigDecimal.valueOf(compra.getParcelas()), 2, RoundingMode.HALF_UP);

            CartaoCredito cartao = compra.getCartaoCredito();
            cartao.setValor(cartao.getValor().subtract(valorParcela));

            compraRepository.save(compra);
            cartaoCreditoRepository.save(cartao);
        } else {
            throw new RuntimeException("Todas as parcelas já foram pagas");
        }
    }

    public BigDecimal calcularTotalComprasEmAberto(Long cartaoId) {
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
