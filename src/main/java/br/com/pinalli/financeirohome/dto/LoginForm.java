package br.com.pinalli.financeirohome.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    @NotNull(message = "Email não pode ser nulo")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotNull(message = "Senha não pode ser nula")
    @NotBlank(message = "Senha não pode estar em branco")
    private String senha;

    public UsernamePasswordAuthenticationToken converter() {
        if (email == null || senha == null) {
            throw new IllegalArgumentException("Email e senha não podem ser nulos");
        }
        return new UsernamePasswordAuthenticationToken(email.trim(), senha);
    }

    // Método para validação adicional se necessário
    public void validate() {
        List<String> errors = new ArrayList<>();

        if (email == null || email.trim().isEmpty()) {
            errors.add("Email é obrigatório");
        }

        if (senha == null || senha.trim().isEmpty()) {
            errors.add("Senha é obrigatória");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
    }
}