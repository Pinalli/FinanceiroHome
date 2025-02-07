package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "relatorios_financeiros_itens")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioFinanceiroItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relatorio_financeiro_id", nullable = false)
    private RelatoriosFinanceiros relatoriosFinanceiros;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_a_pagar_id")
    private ContaPagar contaPagar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_a_receber_id")
    private ContaReceber contaReceber;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private Boolean tipo; // true = receita, false = despesa

    // Getters e Setters (opcional, se não estiver usando Lombok)
}