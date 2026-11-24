package bryan.david.sagsa.services;

import bryan.david.sagsa.exceptions.ObjectNotFoundException;
import bryan.david.sagsa.models.Modulo;
import bryan.david.sagsa.models.Sapz;
import bryan.david.sagsa.models.Usuario;
import bryan.david.sagsa.models.dtos.SapzRequestDTO;
import bryan.david.sagsa.models.dtos.SapzResponseDTO;
import bryan.david.sagsa.repositories.ModuloRepository;
import bryan.david.sagsa.repositories.SapzRepository;
import bryan.david.sagsa.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SapzService {

    private final SapzRepository sapzRepository;
    private final ModuloRepository moduloRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<SapzResponseDTO> listarTodas() {
        return sapzRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public SapzResponseDTO buscarPorId(Long id) {
        Sapz sapz = sapzRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("SAPZ não encontrada com ID: " + id));
        return toResponseDTO(sapz);
    }

    @Transactional
    public SapzResponseDTO criar(SapzRequestDTO dto) {
        Modulo modulo = moduloRepository.findById(dto.idModulo())
                .orElseThrow(() -> new ObjectNotFoundException("Módulo não encontrado com ID: " + dto.idModulo()));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado com ID: " + dto.usuarioId()));

        Sapz sapz = new Sapz();
        sapz.setTitulo(dto.titulo());
        sapz.setDescricao(dto.descricao());
        sapz.setDesafio(dto.desafio());
        sapz.setCargaHorariaTotal(dto.cargaHorariaTotal());
        sapz.setDataGeracao(LocalDateTime.now());
        sapz.setModulo(modulo);
        sapz.setUsuario(usuario);

        Sapz salva = sapzRepository.save(sapz);
        return toResponseDTO(salva);
    }

    @Transactional
    public void deletar(Long id) {
        if (!sapzRepository.existsById(id)) {
            throw new ObjectNotFoundException("SAPZ não encontrada com ID: " + id);
        }
        sapzRepository.deleteById(id);
    }

    private SapzResponseDTO toResponseDTO(Sapz sapz) {
        String nomeUsuario = sapz.getUsuario() != null ? sapz.getUsuario().getNome() : null;
        return new SapzResponseDTO(
                sapz.getId(),
                sapz.getTitulo(),
                sapz.getDescricao(),
                sapz.getDesafio(),
                sapz.getDataGeracao(),
                sapz.getCargaHorariaTotal(),
                nomeUsuario
        );
    }
}