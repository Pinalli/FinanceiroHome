package br.com.pinalli.financeirohome.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "parcela_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParcelaCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "O valor da parcela deve ser positivo")
    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    @FutureOrPresent
    @NotNull(message = "A data de vencimento não pode ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // 🔹 Força o formato correto
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING) // Mapeia o enum como uma string no banco de dados
    @Column(nullable = false)
    private StatusParcelaCompra status; //PENDENTE, PAGA, ATRASADA

    @NotNull
    @Min(1) // Garante que a parcela seja pelo menos 1
    @Column(nullable = false)
    private Integer numeroParcela; // Número da parcela (ex.: 1, 2, 3, etc.)

    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    @JsonBackReference // Evita loop infinito na serialização
    private CompraCartao compra;
}
