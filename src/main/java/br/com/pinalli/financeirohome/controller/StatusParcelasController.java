package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.StatusParcelasDTO;
import br.com.pinalli.financeirohome.model.StatusParcelas;
import br.com.pinalli.financeirohome.service.StatusParcelasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/status-parcelas")
public class StatusParcelasController {

    private final StatusParcelasService statusParcelasService;

    public StatusParcelasController(StatusParcelasService statusParcelasService) {
        this.statusParcelasService = statusParcelasService;
    }

    @PostMapping
    public ResponseEntity<StatusParcelasDTO> criarStatusParcelas(@RequestBody StatusParcelasDTO statusParcelasDTO) {
        StatusParcelas novoStatus = statusParcelasService.criarStatusParcelas(statusParcelasDTO);
        StatusParcelasDTO responseDTO = convertToDTO(novoStatus);
        return ResponseEntity.ok(responseDTO);
    }

    private StatusParcelasDTO convertToDTO(StatusParcelas novoStatus) {
        StatusParcelasDTO dto = new StatusParcelasDTO();
        dto.setId(novoStatus.getId());
        dto.setParcelaId(novoStatus.getParcelaId());
        dto.setStatus(novoStatus.getStatus());
        dto.setDataAlteracao(novoStatus.getDataAlteracao());
        dto.setUsuarioId(novoStatus.getUsuarioId());
        return dto;
    }

    @GetMapping("/parcela/{parcelaId}")
    public ResponseEntity<List<StatusParcelasDTO>> buscarPorParcelaId(@PathVariable Long parcelaId) {
        List<StatusParcelas> status = statusParcelasService.buscarPorParcelaId(parcelaId);
        List<StatusParcelasDTO> responseDTOs = status.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }
}