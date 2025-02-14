package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.CompraCartaoDTO;
import br.com.pinalli.financeirohome.model.CompraCartao;
import br.com.pinalli.financeirohome.service.CompraCartaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/compra")
public class CompraCartaoController {

    private final CompraCartaoService compraCartaoService;

    public CompraCartaoController(CompraCartaoService compraCartaoService) {
        this.compraCartaoService = compraCartaoService;
    }

    @PostMapping("/{cartaoId}/compra")
    public ResponseEntity<CompraCartaoDTO> criarCompra(@PathVariable Long cartaoId,
                                                       @Valid @RequestBody CompraCartaoDTO dto) {
        CompraCartao compra = compraCartaoService.criarCompra(dto, cartaoId);

        CompraCartaoDTO response = new CompraCartaoDTO();
        response.setId(compra.getId()); // Agora `setId` existe no DTO
        response.setDescricao(compra.getDescricao());
        response.setValorTotal(compra.getValorTotal());
        response.setDataCompra(compra.getDataCompra());
        response.setCategoria(compra.getCategoria().toString());
        response.setQuantidadeParcelas(compra.getQuantidadeParcelas());
        response.setCartaoId(compra.getCartao().getId());
        response.setUsuarioId(compra.getUsuario().getId());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/compra/{compraId}")
    public ResponseEntity<CompraCartaoDTO> buscarCompra(@PathVariable Long cartaoId,
                                                        @PathVariable Long compraId) {
        CompraCartao compraCartao = compraCartaoService.buscarCompraPorId(cartaoId, compraId);
        return ResponseEntity.ok(convertToDTO(compraCartao));
    }

    private CompraCartaoDTO convertToDTO(CompraCartao compraCartao) {
        CompraCartaoDTO dto = new CompraCartaoDTO();
        dto.setId(compraCartao.getId());
        dto.setDescricao(compraCartao.getDescricao());
        dto.setValorTotal(compraCartao.getValorTotal());
        dto.setDataCompra(compraCartao.getDataCompra());
        dto.setCategoria(compraCartao.getCategoria().toString());
        dto.setQuantidadeParcelas(compraCartao.getQuantidadeParcelas());
        dto.setCartaoId(compraCartao.getCartao().getId());
        dto.setUsuarioId(compraCartao.getUsuario().getId());
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<CompraCartaoDTO>> listarComprasPorCartao(@PathVariable Long cartaoId) {
        List<CompraCartao> compras = compraCartaoService.listarComprasPorCartao(cartaoId);
        List<CompraCartaoDTO> comprasDTO = compras.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comprasDTO);
    }

}
