package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CartaoCreditoDTO;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartaoCreditoService {

    private final CartaoCreditoRepository cartaoCreditoRepository;

    @Autowired
    public CartaoCreditoService(CartaoCreditoRepository cartaoCreditoRepository) {
        this.cartaoCreditoRepository = cartaoCreditoRepository;
    }

    public CartaoCreditoDTO criarCartaoCredito(CartaoCreditoDTO cartaoCreditoDTO) {
        CartaoCredito cartaoCredito = cartaoCreditoDTO.toEntity();
        CartaoCredito savedCartaoCredito = cartaoCreditoRepository.save(cartaoCredito);
        return CartaoCreditoDTO.fromEntity(savedCartaoCredito);
    }

    public CartaoCreditoDTO buscarCartaoCreditoPorId(Long id) {
        return cartaoCreditoRepository.findById(id)
                .map(CartaoCreditoDTO::fromEntity)
                .orElse(null);
    }

    public CartaoCreditoDTO atualizarCartaoCredito(CartaoCreditoDTO cartaoCreditoDTO) {
        if (cartaoCreditoRepository.existsById(cartaoCreditoDTO.getId())) {
            CartaoCredito cartaoCredito = cartaoCreditoDTO.toEntity();
            CartaoCredito updatedCartaoCredito = cartaoCreditoRepository.save(cartaoCredito);
            return CartaoCreditoDTO.fromEntity(updatedCartaoCredito);
        }
        return null;
    }

    public void deletarCartaoCredito(Long id) {
        cartaoCreditoRepository.deleteById(id);
    }
}