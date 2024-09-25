package br.com.pinalli.financeirohome.security;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;


import java.util.Collection;


@Getter
@Setter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private final Long id;

    public CustomUserDetails(Long id, String username, String password,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

}