package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.dto.ParcelaDTO;
import br.com.pinalli.financeirohome.service.ParcelaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parcelas")
public class ParcelaController {
    @Autowired
    private ParcelaService parcelaService;

   // @PostMapping
  //  public ResponseEntity<ParcelaDTO> salvarParcela(@RequestBody ParcelaDTO parcelaDTO) {
      //  ParcelaDTO parcelaSalva = parcelaService.salvarParcela(parcelaDTO);
     //   return ResponseEntity.status(HttpStatus.CREATED).body(parcelaSalva);
//    }

    // Outros métodos para buscar, atualizar, excluir parcelas
}