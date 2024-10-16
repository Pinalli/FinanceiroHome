package br.com.pinalli.financeirohome.model;

import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contas_a_pagar")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class ContaPagar {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConta status; // Agora é do tipo StatusConta

    private String categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Construtor (importante)
    public ContaPagar() {}


    public ContaPagar(String descricao, BigDecimal valor, LocalDate dataVencimento, StatusConta status, String categoria, UsuarioDTO usuario) {
    }
}