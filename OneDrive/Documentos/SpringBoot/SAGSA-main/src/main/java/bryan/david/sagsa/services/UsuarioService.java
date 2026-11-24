package bryan.david.sagsa.services;

import bryan.david.sagsa.exceptions.ObjectNotFoundException;
import bryan.david.sagsa.models.Usuario;
import bryan.david.sagsa.models.dtos.UsuarioRequestDTO;
import bryan.david.sagsa.models.dtos.UsuarioResponseDTO;
import bryan.david.sagsa.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado com ID: " + id));
        return toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setTipoPerfil(dto.tipoPerfil());

        Usuario salvo = usuarioRepository.save(usuario);
        return toResponseDTO(salvo);
    }

    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ObjectNotFoundException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoPerfil()
        );
    }
}