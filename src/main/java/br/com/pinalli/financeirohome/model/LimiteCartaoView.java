package br.com.pinalli.financeirohome.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Immutable
@Subselect("SELECT * FROM view_limite_cartao")
public class LimiteCartaoView {

    @Id
    private Long id;

    @Column(name = "limite_total")
    private BigDecimal limiteTotal;

    @Column(name = "limite_disponivel")
    private BigDecimal limiteDisponivel;

}