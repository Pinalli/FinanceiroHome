package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.StatusParcelaCompraDTO;
import br.com.pinalli.financeirohome.model.StatusParcelaCompra;
import br.com.pinalli.financeirohome.service.StatusParcelaCompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/status-parcelas")
public class StatusParcelasController {

    private final StatusParcelaCompraService statusParcelasService;

    public StatusParcelasController(StatusParcelaCompraService statusParcelasService) {
        this.statusParcelasService = statusParcelasService;
    }

    @PostMapping
    public ResponseEntity<StatusParcelaCompraDTO> criarStatusParcelas(@RequestBody StatusParcelaCompraDTO statusParcelasDTO) {
        // Chama o serviço para criar o StatusParcelaCompra com base no DTO
        StatusParcelaCompra novoStatus = statusParcelasService.criarStatusParcelas(statusParcelasDTO);

        // Converte a entidade criada para DTO para retornar na resposta
        StatusParcelaCompraDTO responseDTO = convertToDTO(novoStatus);

        // Retorna o DTO com o status HTTP 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    private StatusParcelaCompraDTO convertToDTO(StatusParcelaCompra novoStatus) {
        StatusParcelaCompraDTO dto = new StatusParcelaCompraDTO();
        dto.setId(novoStatus.getId());
        dto.setParcelaId(novoStatus.getParcela().getId());
        dto.setStatus(novoStatus.getStatus());
        dto.setDataAlteracao(novoStatus.getDataAlteracao());
        dto.setUsuarioId(novoStatus.getUsuarioId());
        return dto;
    }
    @GetMapping("/parcela/{parcelaId}")
    public ResponseEntity<List<StatusParcelaCompraDTO>> buscarPorParcelaId(@PathVariable Long parcelaId) {
        // Busca os StatusParcelaCompra pelo parcelaId
        List<StatusParcelaCompra> status = statusParcelasService.buscarPorParcelaId(parcelaId);

        // Converte a lista de StatusParcelaCompra para DTOs
        List<StatusParcelaCompraDTO> responseDTOs = status.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // Retorna a lista de StatusParcelaCompraDTO
        return ResponseEntity.ok(responseDTOs);
    }
}
