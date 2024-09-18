package br.com.pinalli.financeirohome.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import org.apache.catalina.Role;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;  
    private String email;
    private String senha;

    @OneToMany(mappedBy = "usuario")
    private List<Conta> contas;

    // Add a new constructor with three String arguments
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER, ADMIN
    }
}
