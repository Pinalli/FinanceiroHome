package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.repository.ParcelaCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParcelaCompraService {
    @Autowired
    private ParcelaCompraRepository parcelaRepository;

  //  public ParcelaCompraDTO salvarParcela(ParcelaCompraDTO parcelaDTO) {
       // Parcelas parcela = modelMapper.map(parcelaDTO, Parcelas.class);
     //   parcela = parcelaRepository.save(parcela);
    //    return modelMapper.map(parcela, ParcelaCompraDTO.class);
 //   }
}
