package bryan.david.sagsa.services;

import bryan.david.sagsa.models.Usuario;
import bryan.david.sagsa.repositories.UsuarioRepository;
import bryan.david.sagsa.security.UserSpringSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Utilizador não encontrado: " + email);
        }

        Usuario usuario = usuarioOpt.get();
        
        // Extrai o nome do Enum TipoPerfil (ex: "ROLE_DOCENTE" ou "ROLE_ADMIN")
        String perfilNome = usuario.getTipoPerfil() != null ? usuario.getTipoPerfil().name() : "ROLE_DOCENTE";

        return new UserSpringSecurity(
                usuario.getIdUsuario(), 
                usuario.getEmail(), 
                usuario.getSenha(), 
                perfilNome
        );
    }
}