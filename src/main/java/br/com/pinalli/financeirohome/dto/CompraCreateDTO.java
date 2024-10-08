package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.Compras;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CompraCreateDTO {
    @NotNull
    private LocalDate dataCompra;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal valor;

    @NotBlank
    private String descricao;

    @NotBlank
    private String categoria;

    @Min(1)
    private int parcelas;

    @Min(0)
    private int parcelasPagas;


    public static ComprasDTO fromEntity(Compras compra) {
        return ComprasDTO.fromEntity(compra);
    }

}