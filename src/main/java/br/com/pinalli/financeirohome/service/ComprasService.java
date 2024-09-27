package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.ComprasDTO;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.Compras;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.repository.ComprasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComprasService {

    private final ComprasRepository comprasRepository;
    private final CartaoCreditoRepository cartaoCreditoRepository;

    @Autowired
    public ComprasService(ComprasRepository comprasRepository, CartaoCreditoRepository cartaoCreditoRepository) {
        this.comprasRepository = comprasRepository;
        this.cartaoCreditoRepository = cartaoCreditoRepository;
    }

    @Transactional
    public ComprasDTO registrarCompra(ComprasDTO comprasDTO, Long cartaoId) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão de crédito não encontrado"));
        Compras compra = comprasDTO.toEntity();
        compra.setCartaoCredito(cartao);
        Compras compraSalva = comprasRepository.save(compra);
        return ComprasDTO.fromEntity(compraSalva);
    }

    public List<ComprasDTO> listarComprasPorCartao(Long cartaoId) {
        List<Compras> compras = comprasRepository.findByCartaoCreditoId(cartaoId);
        return compras.stream().map(ComprasDTO::fromEntity).collect(Collectors.toList());
    }

    public ComprasDTO buscarCompraPorId(Long compraId) {
        return comprasRepository.findById(compraId)
                .map(ComprasDTO::fromEntity)
                .orElse(null);
    }

    @Transactional
    public ComprasDTO atualizarCompra(ComprasDTO compraAtualizada) {
        if (comprasRepository.existsById(compraAtualizada.getId())) {
            Compras compra = compraAtualizada.toEntity();
            Compras compraSalva = comprasRepository.save(compra);
            return ComprasDTO.fromEntity(compraSalva);
        }
        return null;
    }

    @Transactional
    public void deletarCompra(Long compraId) {
        comprasRepository.deleteById(compraId);
    }
}