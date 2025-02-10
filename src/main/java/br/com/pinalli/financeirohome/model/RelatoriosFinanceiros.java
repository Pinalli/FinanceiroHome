package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "relatorios_financeiros")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatoriosFinanceiros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @OneToMany(mappedBy = "relatorioFinanceiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RelatorioFinanceiroItem> itens;


}