package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "status_parcela_compra")
public class StatusParcelaCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parcela_id") // Campo é uma entidade!
    private ParcelaCompra parcela;

    @Convert(converter = StatusContaEnumConverter.class)
    @Column(name = "status", nullable = false)
    private StatusConta status;

    @Column(name = "data_alteracao", nullable = false)
    private LocalDateTime dataAlteracao;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Long getUsuarioId() {
        if (usuario != null) {
            return usuario.getId();
        }
        return null;
    }

    public void setUsuarioId(Long usuarioId) {
        if (usuario == null) {
            usuario = new Usuario();
        }
        usuario.setId(usuarioId);
    }


}