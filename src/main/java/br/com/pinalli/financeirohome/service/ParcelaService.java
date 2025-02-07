package br.com.pinalli.financeirohome.service;

import br.com.pinalli.financeirohome.repository.ParcelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParcelaService {
    @Autowired
    private ParcelaRepository parcelaRepository;

  //  public ParcelaDTO salvarParcela(ParcelaDTO parcelaDTO) {
       // Parcelas parcela = modelMapper.map(parcelaDTO, Parcelas.class);
     //   parcela = parcelaRepository.save(parcela);
    //    return modelMapper.map(parcela, ParcelaDTO.class);
 //   }
}
