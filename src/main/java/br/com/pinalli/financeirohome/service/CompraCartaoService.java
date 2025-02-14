package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.CompraCartaoDTO;
import br.com.pinalli.financeirohome.model.*;
import br.com.pinalli.financeirohome.repository.CartaoCreditoRepository;
import br.com.pinalli.financeirohome.repository.CategoriaRepository;
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
    private final CategoriaRepository categoriaRepository;
    private final ParcelaCompraService parcelaService;

    public CompraCartaoService(CompraCartaoRepository compraCartaoRepository,
                               UsuarioRepository usuarioRepository,
                               CartaoCreditoRepository cartaoRepository, CategoriaRepository categoriaRepository, ParcelaCompraService parcelaService) {
        this.compraCartaoRepository = compraCartaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cartaoRepository = cartaoRepository;
        this.categoriaRepository = categoriaRepository;
        this.parcelaService = parcelaService;
    }

    public CompraCartao criarCompra(CompraCartaoDTO dto, Long cartaoId) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        CartaoCredito cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado"));

        Categoria categoria = categoriaRepository.findByNome(dto.getCategoria())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + dto.getCategoria()));

        CompraCartao compra = new CompraCartao();
        compra.setDescricao(dto.getDescricao());
        compra.setValorTotal(dto.getValorTotal());
        compra.setDataCompra(dto.getDataCompra());
        compra.setCategoria(categoria);
        compra.setUsuario(usuario);
        compra.setCartao(cartao);

        if (dto.isParcelado()) {
            List<ParcelaCompra> parcelas = parcelaService.criarParcelas(compra, dto.getQuantidadeParcelas());
            compra.setParcelas(parcelas);
        }

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


