package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.StatusParcelaCompraDTO;
import br.com.pinalli.financeirohome.model.ParcelaCompra;
import br.com.pinalli.financeirohome.model.StatusParcelaCompra;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.ParcelaCompraRepository;
import br.com.pinalli.financeirohome.repository.StatusParcelaCompraRepository;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusParcelaCompraService {

    private final StatusParcelaCompraRepository statusParcelaCompraRepository;
    private final ParcelaCompraRepository parcelaCompraRepository;
    private final UsuarioRepository usuarioRepository;

    public StatusParcelaCompraService(StatusParcelaCompraRepository statusParcelaCompraRepository,
                                      ParcelaCompraRepository parcelaCompraRepository,
                                      UsuarioRepository usuarioRepository) {
        this.statusParcelaCompraRepository = statusParcelaCompraRepository;
        this.parcelaCompraRepository = parcelaCompraRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public StatusParcelaCompra criarStatusParcelas(StatusParcelaCompraDTO statusDTO) {
        // Busca a ParcelaCompra com o parcelaId
        ParcelaCompra parcela = parcelaCompraRepository.findById(statusDTO.getParcelaId())
                .orElseThrow(() -> new EntityNotFoundException("Parcela não encontrada"));

        // Busca o Usuário com o usuarioId
        Usuario usuario = usuarioRepository.findById(statusDTO.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // Cria o objeto StatusParcelaCompra com os dados do DTO
        StatusParcelaCompra status = new StatusParcelaCompra();
        status.setParcela(parcela);
        status.setStatus(statusDTO.getStatus());
        status.setDataAlteracao(statusDTO.getDataAlteracao());
        status.setUsuario(usuario);

        // Salva a entidade no banco
        return statusParcelaCompraRepository.save(status);
    }

    public List<StatusParcelaCompra> buscarPorParcelaId(Long parcelaId) {
        // Busca os StatusParcelaCompra pelo parcelaId
        return statusParcelaCompraRepository.findByParcela_Id(parcelaId);
    }
}
