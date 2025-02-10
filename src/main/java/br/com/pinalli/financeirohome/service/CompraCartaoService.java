package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CompraCartaoDTO;
import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.CompraCartao;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.repository.CompraCartaoRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraCartaoService {

    private final CompraCartaoRepository compraCartaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CartaoCreditoRepository cartaoRepository;

    public CompraCartaoService(CompraCartaoRepository compraCartaoRepository,
                               UsuarioRepository usuarioRepository,
                               CartaoCreditoRepository cartaoRepository) {
        this.compraCartaoRepository = compraCartaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cartaoRepository = cartaoRepository;
    }

    public CompraCartao criarCompra(CompraCartaoDTO dto, Long cartaoId) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        CartaoCredito cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado"));

        CompraCartao compra = new CompraCartao();
        compra.setDescricao(dto.getDescricao());
        compra.setValor(dto.getValor());
        compra.setDataCompra(dto.getDataCompra());
        compra.setCategoria(dto.getCategoria());
        compra.setParcelas(dto.getParcelas());
        compra.setUsuario(usuario);
        compra.setCartao(cartao);

        return compraCartaoRepository.save(compra);
    }




    public List<CompraCartao> listarComprasPorCartao(Long cartaoId) {
        return compraCartaoRepository.findByCartaoId(cartaoId);
    }

    public CompraCartao buscarCompraPorId(Long cartaoId, Long compraId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado.");
        }

        return compraCartaoRepository.findByCartaoIdAndId(cartaoId, compraId)
                .orElseThrow(() -> new EntityNotFoundException("Compra não encontrada para o cartão informado."));
    }
}


