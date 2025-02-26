package br.com.pinalli.financeirohome.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compra_cartao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraCartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição não pode estar em branco")
    private String descricao;

    @Positive(message = "O valor total deve ser um valor positivo")
    @Column(precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Min(value = 1, message = "A quantidade de parcelas deve ser pelo menos 1")
    private int quantidadeParcelas;

    @NotNull(message = "A data da compra não pode ser nula")
    private LocalDate dataCompra;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    private CartaoCredito cartao;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Evita problemas de serialização no retorno JSON
    private List<ParcelaCompra> parcelas = new ArrayList<>();
}
