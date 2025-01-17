package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.dto.ParcelaDTO;
import br.com.pinalli.financeirohome.model.Parcela;
import br.com.pinalli.financeirohome.repository.ParcelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParcelaService {
    @Autowired
    private ParcelaRepository parcelaRepository;

  //  public ParcelaDTO salvarParcela(ParcelaDTO parcelaDTO) {
       // Parcela parcela = modelMapper.map(parcelaDTO, Parcela.class);
     //   parcela = parcelaRepository.save(parcela);
    //    return modelMapper.map(parcela, ParcelaDTO.class);
 //   }
}
