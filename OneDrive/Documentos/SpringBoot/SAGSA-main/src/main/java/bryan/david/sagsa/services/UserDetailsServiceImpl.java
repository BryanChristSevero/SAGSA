package bryan.david.sagsa.services;

import bryan.david.sagsa.models.Usuario;
import bryan.david.sagsa.repositories.UsuarioRepository;
import bryan.david.sagsa.security.UserSpringSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Assume que existe um método no UsuarioRepository para buscar por email
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Utilizador não encontrado: " + email);
        }

        Usuario usuario = usuarioOpt.get();
        // Como o modelo do DDL usa relacionamento com perfis, ajustamos conforme o objeto
        String perfilNome = usuario.getPerfil() != null ? usuario.getPerfil().getNome() : "USER";

        return new UserSpringSecurity(usuario.getId(), usuario.getEmail(), usuario.getSenhaHash(), perfilNome);
    }
}