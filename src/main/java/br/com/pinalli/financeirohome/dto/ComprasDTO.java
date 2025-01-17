package br.com.pinalli.financeirohome.dto;

import br.com.pinalli.financeirohome.model.Compras;
import br.com.pinalli.financeirohome.model.Parcela;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.service.ComprasService;
import lombok.*;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    private static final Logger log = LoggerFactory.getLogger(ComprasService.class); // For SLF4j

    private BigDecimal valorParcela;
    private BigDecimal limiteDisponivelMomentoCompra;
    private Long id;

    @Getter
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

    @NotNull(message = "Cartão de crédito não pode ser nulo")
    private CartaoCreditoDTO cartaoCredito;

    @Min(value = 1, message = "Número de parcelas deve ser pelo menos 1")
    private Integer parcelas;

    private List<Parcela> listaParcelas;

    private Long usuarioId; // Adicionar o campo para referenciar o usuário




    @NotNull(message = "Status da compra não pode ser nulo")
    private Compras.StatusCompra status;


    public static ComprasDTO fromEntity(Compras compra) {
        if (compra == null) return null;

        return ComprasDTO.builder()
                .id(compra.getId())
                .dataCompra(LocalDate.from(compra.getDataCompra()))
                .valor(compra.getValor())
                .descricao(compra.getDescricao())
                .categoria(compra.getCategoria())
                .listaParcelas(compra.getParcelas())// Atribui a lista de parcelas
             //   .parcelas(compra.getParcelas()) // Atribui a lista de parcelas
                .cartaoCredito(CartaoCreditoDTO.converterParaDTO(compra.getCartaoCredito()))
                .usuarioId(compra.getUsuario().getId())

                .build();
    }

    public Compras toEntity() {
        // Supondo que você tenha o ID do usuário no DTO, você precisa criar um objeto Usuario
        Usuario usuario = new Usuario();

        log.debug("Convertendo ComprasDTO para Compras entity");
        log.debug("compraId: {}", this.id);
        log.debug("cartaoCredito: {}", this.cartaoCredito);
        log.debug("valor: {}", this.valor);
        log.debug("dataCompra: {}", this.dataCompra);
        log.debug("descricao: {}", this.descricao);
        log.debug("categoria: {}", this.categoria);
        log.debug("parcelas: {}", this.parcelas);
        log.debug("status: {}", this.status);
        log.debug("usuario_id: {}", this.usuarioId);

        if (this.cartaoCredito == null || this.usuarioId == null) {
            throw new IllegalArgumentException("Dados inválidos para criar a compra.");
        }

      // if (this.cartaoCredito == null) throw new IllegalArgumentException("CartaoCredito não pode ser nulo");
        if (this.cartaoCredito.getId() == null) throw new IllegalArgumentException("ID do CartaoCredito não pode ser nulo");
        if (this.cartaoCredito.getUsuarioId() == null) throw new IllegalArgumentException("UsuarioId do CartaoCredito não pode ser nulo");
        if (this.valor == null) throw new IllegalArgumentException("Valor não pode ser nulo");
        if (this.dataCompra == null) throw new IllegalArgumentException("Data da compra não pode ser nula");
        if (this.descricao == null) throw new IllegalArgumentException("Descrição não pode ser nula");
        if (this.categoria == null) throw new IllegalArgumentException("Categoria não pode ser nula");
        if (this.parcelas == null) throw new IllegalArgumentException("Parcelas não pode ser nulo");
        if (this.status == null) throw new IllegalArgumentException("O Status não pode ser nulo");
        usuario.setId(this.usuarioId);  // Certifique-se de que o DTO tenha o campo usuarioId
        // Calculando o número de parcelas pagas
     //   int parcelasPagas = parcelas.stream()
        //        .filter(Parcela::isPaga)
        //        .count();
        return Compras.builder()
                .id(this.id)
                .dataCompra(this.dataCompra.atStartOfDay())
                .valor(this.valor)
                .descricao(this.descricao)
                .categoria(this.categoria)
           //     .parcelas(this.parcelas)
          //      .parcelasPagas(parcelasPagas)
                .cartaoCredito(this.cartaoCredito.toEntity())
                .usuario(usuario)  // Passando o objeto Usuario
                .status(this.status)
                .build();
    }


}
