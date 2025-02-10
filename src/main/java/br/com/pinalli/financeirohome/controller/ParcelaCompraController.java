package br.com.pinalli.financeirohome.controller;

import br.com.pinalli.financeirohome.service.ParcelaCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parcelas")
public class ParcelaCompraController {
    @Autowired
    private ParcelaCompraService parcelaService;


}