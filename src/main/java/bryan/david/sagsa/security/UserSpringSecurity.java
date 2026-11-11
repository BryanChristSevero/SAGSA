package bryan.david.sagsa.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import bryan.david.sagsa.models.Usuario;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserSpringSecurity implements UserDetails {

    private Long id;
    private String email;
    private String senha;
    private Collection<? extends GrantedAuthority> authorities;

    public UserSpringSecurity(Long id, String email, String senha, String perfilNome) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        // Atribui a permissão com base no perfil do utilizador (ex: ROLE_INSTRUTOR, ROLE_ADMINISTRADOR)
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + perfilNome));
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}