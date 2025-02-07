package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.CartaoCredito;
import br.com.pinalli.financeirohome.model.Compras;
import br.com.pinalli.financeirohome.model.Parcelas;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.service.ComprasService;
import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprasDTO {

    private static final Logger log = LoggerFactory.getLogger(ComprasService.class);

    private CartaoCredito cartaoCredito;
    private Long cartaoCreditoId;
    private BigDecimal valorParcela;
    private BigDecimal limiteDisponivelMomentoCompra;
    private Integer parcelasPagas;
    private Long id;

    @NotNull(message = "Data da compra não pode ser nula")
    private LocalDate dataCompra;

    @NotNull(message = "Valor não pode ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    @NotBlank(message = "Descrição não pode ser vazia")
    @Size(max = 255, message = "Descrição não pode ter mais que 255 caracteres")
    private String descricao;

    @NotBlank(message = "Categoria não pode ser vazia")
    private String categoria;

    @Min(value = 1, message = "Número de parcelas deve ser pelo menos 1")
    private Integer parcelas; // ALTERADO PARA INTEGER

    private List<Parcelas> listaParcelas; // Adicionada lista para armazenar as parcelas

    private Long usuarioId;

    @NotNull(message = "Status da compra não pode ser nulo")
    private Compras.StatusCompra status;

    public static ComprasDTO fromEntity(Compras compra) {
        if (compra == null) return null;

        return ComprasDTO.builder()
                .id(compra.getId())
                .dataCompra(compra.getDataCompra() != null ? compra.getDataCompra().toLocalDate() : null)
                .valor(compra.getValor())
                .descricao(compra.getDescricao())
                .categoria(compra.getCategoria())
                .parcelas(compra.getParcelas() != null ? compra.getParcelas() : 1) // Agora correto
                .parcelasPagas(compra.getParcelasPagas() != null ? compra.getParcelasPagas() : 0)
                .listaParcelas(compra.getListaParcelas())
                .cartaoCredito(compra.getCartaoCredito())
                .usuarioId(compra.getUsuario() != null ? compra.getUsuario().getId() : null)
                .status(compra.getStatus())
                .build();
    }

    public Compras toEntity() {
        if (this.cartaoCredito == null || this.cartaoCredito.getId() == null) {
            throw new IllegalArgumentException("Cartão de crédito não pode ser nulo e deve ter um ID.");
        }
        if (this.usuarioId == null) {
            throw new IllegalArgumentException("Usuário ID não pode ser nulo.");
        }
        if (this.valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo.");
        }
        if (this.dataCompra == null) {
            throw new IllegalArgumentException("Data da compra não pode ser nula.");
        }
        if (this.descricao == null) {
            throw new IllegalArgumentException("Descrição não pode ser nula.");
        }
        if (this.categoria == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula.");
        }
        if (this.parcelas == null || this.parcelas < 1) {
            throw new IllegalArgumentException("Número de parcelas deve ser pelo menos 1.");
        }
        if (this.status == null) {
            throw new IllegalArgumentException("Status da compra não pode ser nulo.");
        }

        log.debug("Convertendo ComprasDTO para Compras entity: id={}, cartaoCreditoId={}, valor={}, dataCompra={}, descricao={}, categoria={}, parcelas={}, status={}, usuarioId={}",
                this.id, this.cartaoCredito.getId(), this.valor, this.dataCompra, this.descricao, this.categoria, this.parcelas, this.status, this.usuarioId);

        Usuario usuario = new Usuario();
        usuario.setId(this.usuarioId);

        // Criar lista de parcelas
        List<Parcelas> listaParcelas = criarParcelas(this.dataCompra, this.valor, this.parcelas);

        Compras compra = Compras.builder()
                .id(this.id)
                .dataCompra(this.dataCompra.atStartOfDay())
                .valor(this.valor)
                .descricao(this.descricao)
                .categoria(this.categoria)
                .parcelas(this.parcelas) // AGORA É INTEGER, NÃO LISTA
                .parcelasPagas(this.parcelasPagas != null ? this.parcelasPagas : 0)
                .cartaoCredito(this.cartaoCredito.toEntity())
                .usuario(usuario)
                .status(this.status)
                .valorParcela(this.valorParcela)
                .limiteDisponivelMomentoCompra(this.limiteDisponivelMomentoCompra)
                .dataLimite(this.dataCompra.plusMonths(this.parcelas).atStartOfDay())
                .valorTotal(this.valor)
                .build();

        listaParcelas.forEach(parcela -> parcela.setCompra(compra));

        return compra;
    }

    private List<Parcelas> criarParcelas(LocalDate dataCompra, BigDecimal valorTotal, Integer parcelas) {
        List<Parcelas> listaParcelas = new ArrayList<>();
        if (parcelas > 0) {
            BigDecimal valorParcela = valorTotal.divide(BigDecimal.valueOf(parcelas), 2, RoundingMode.HALF_UP);
            for (int i = 1; i <= parcelas; i++) {
                Parcelas parcela = new Parcelas();
                parcela.setNumeroParcela(i);
                parcela.setValorParcela(valorParcela);
                parcela.setDataVencimento(dataCompra.plusMonths(i - 1));
                parcela.setStatus(Parcelas.StatusParcela.PENDENTE);
                listaParcelas.add(parcela);
            }
        }
        return listaParcelas;
    }
}
