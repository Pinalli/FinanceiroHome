package br.com.pinalli.financeirohome.model;

import br.com.pinalli.financeirohome.dto.UsuarioDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "conta_pagar")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class ContaPagar {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_vencimento", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConta status; // Agora é do tipo StatusConta

    private String categoria;

    // Construtor (importante)
    public ContaPagar() {}


    public ContaPagar(String descricao, BigDecimal valor, LocalDate dataVencimento, StatusConta status, String categoria, Usuario usuario) {
        this.descricao = descricao;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.status = status;
        this.categoria = categoria;
        this.usuario = usuario;
    }
}