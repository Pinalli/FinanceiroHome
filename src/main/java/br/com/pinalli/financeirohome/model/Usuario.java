package br.com.pinalli.financeirohome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Usuario implements UserDetails {

    /**
     *
     *  id SERIAL PRIMARY KEY,
     *                          nome VARCHAR(100) NOT NULL,
     *                          email VARCHAR(100) UNIQUE NOT NULL,
     *                          senha VARCHAR(255) NOT NULL,
     *                          data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     *                          data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private LocalDateTime data_criacao;

    @Column(nullable = false)
    private LocalDateTime data_atualizacao;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContaPagar> contasPagar;

    public Usuario(String usuarioTeste, String email, String senha123) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); // Única role
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}