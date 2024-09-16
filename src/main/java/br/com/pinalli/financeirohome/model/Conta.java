package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private Double valor;
    private Boolean tipo; // true = conta a pagar, false = conta a receber
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public class ContaService {
        public void criarConta(String descricao, double valor, boolean tipo, int usuarioId) {
            // criar uma nova conta no banco de dados
            Conta conta = new Conta();
            conta.setDescricao(descricao);
            conta.setValor(valor);
            conta.setTipo(tipo);
            // conta.setUsuarioId(usuarioId);

            // inserir a conta no banco de dados
            // ...
        }
    }
}