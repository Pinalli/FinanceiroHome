package br.com.pinalli.financeirohome.auth;

import br.com.pinalli.financeirohome.dto.CartaoCreditoRequest;
import br.com.pinalli.financeirohome.model.Usuario;
import br.com.pinalli.financeirohome.repository.UsuarioRepository;
import br.com.pinalli.financeirohome.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartaoCreditoAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationFilter jwtTokenFilter;

    private String jwtToken;
    private String invalidToken = "invalidToken123";

    @BeforeEach
    public void setup() {
        // Criar um usuário de teste e gerar um token JWT válido
        Usuario usuario = new Usuario();
        usuario.setNome("Teste Auth");
        usuario.setEmail("teste.auth@example.com");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuarioRepository.save(usuario);

      //  jwtToken = jwtTokenFilter.generateToken(usuario.getEmail());
    }

    @Test
    public void testCriarCartaoCreditoComTokenValido() throws Exception {
        CartaoCreditoRequest dto = new CartaoCreditoRequest();
        dto.setBandeiraCartao("Cartão de Teste Auth");
        dto.setLimite(new BigDecimal("1000.00"));
        dto.setValor(new BigDecimal("0.00"));

        mockMvc.perform(post("/api/cartoes-credito")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Cartão de Teste Auth"));
    }

    @Test
    public void testCriarCartaoCreditoComTokenInvalido() throws Exception {
        CartaoCreditoRequest dto = new CartaoCreditoRequest();
        dto.setBandeiraCartao("Cartão de Teste Auth Inválido");
        dto.setLimite(new BigDecimal("1000.00"));
        dto.setValor(new BigDecimal("0.00"));

        mockMvc.perform(post("/api/cartoes-credito")
                        .header("Authorization", "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testBuscarCartaoCreditoSemToken() throws Exception {
        mockMvc.perform(get("/api/cartoes-credito/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAtualizarCartaoCreditoComTokenValido() throws Exception {
        // Primeiro, crie um cartão de crédito
        CartaoCreditoRequest dto = new CartaoCreditoRequest();
        dto.setBandeiraCartao("Cartão Original Auth");
        dto.setLimite(new BigDecimal("3000.00"));
        dto.setValor(new BigDecimal("0.00"));

        String response = mockMvc.perform(post("/api/cartoes-credito")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        CartaoCreditoRequest createdDto = objectMapper.readValue(response, CartaoCreditoRequest.class);

        // Atualize o cartão
        createdDto.setBandeiraCartao("Cartão Atualizado Auth");

        mockMvc.perform(put("/api/cartoes-credito/" + createdDto.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Cartão Atualizado Auth"));
    }

    @Test
    public void testDeletarCartaoCreditoComTokenInvalido() throws Exception {
        mockMvc.perform(delete("/api/cartoes-credito/1")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized());
    }
}