package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.StatusParcelasDTO;
import br.com.pinalli.financeirohome.model.StatusConta;
import br.com.pinalli.financeirohome.model.StatusParcelas;
import br.com.pinalli.financeirohome.repository.StatusParcelaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusParcelasService {

    private final StatusParcelaRepository statusParcelaRepository;

    public StatusParcelasService(StatusParcelaRepository statusParcelaRepository) {
        this.statusParcelaRepository = statusParcelaRepository;
    }

    public StatusParcelas criarStatusParcelas(StatusParcelasDTO statusParcelasDTO) {
        StatusParcelas statusParcelas = new StatusParcelas();
        statusParcelas.setParcelaId(statusParcelasDTO.getParcelaId());

        StatusConta statusEnum = StatusConta.valueOf(String.valueOf(statusParcelasDTO.getStatus()));
        statusParcelas.setStatus(statusEnum);

        statusParcelas.setDataAlteracao(statusParcelasDTO.getDataAlteracao());
        statusParcelas.setUsuarioId(statusParcelasDTO.getUsuarioId());
        return statusParcelaRepository.save(statusParcelas);
    }

    public List<StatusParcelas> buscarPorParcelaId(Long parcelaId) {
        return statusParcelaRepository.findByParcela_Id(parcelaId);
    }
}