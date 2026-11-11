package bryan.david.sagsa.services;

import bryan.david.sagsa.dtos.SapzRequestDTO;
import bryan.david.sagsa.dtos.SapzResponseDTO;
import bryan.david.sagsa.models.Sapz;
import bryan.david.sagsa.models.Turma;
import bryan.david.sagsa.models.UnidadeCurricular;
import bryan.david.sagsa.models.Usuario;
import bryan.david.sagsa.repositories.SapzRepository;
import bryan.david.sagsa.repositories.TurmaRepository;
import bryan.david.sagsa.repositories.UnidadeCurricularRepository;
import bryan.david.sagsa.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SapzService {

    private final SapzRepository sapzRepository;
    private final UnidadeCurricularRepository unidadeCurricularRepository;
    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository; // Opcional, caso use turmas

    @Transactional(readOnly = true)
    public List<SapzResponseDTO> listarTodas() {
        return sapzRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public SapzResponseDTO buscarPorId(Long id) {
        Sapz sapz = sapzRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SAPZ não encontrada com o ID: " + id));
        return toResponseDTO(sapz);
    }

    @Transactional
    public SapzResponseDTO criar(SapzRequestDTO dto) {
        UnidadeCurricular uc = unidadeCurricularRepository.findById(dto.unidadeCurricularId())
                .orElseThrow(() -> new EntityNotFoundException("Unidade Curricular não encontrada"));

        Usuario instrutor = usuarioRepository.findById(dto.instrutorId())
                .orElseThrow(() -> new EntityNotFoundException("Instrutor não encontrado"));

        Turma turma = null;
        if (dto.turmaId() != null) {
            turma = turmaRepository.findById(dto.turmaId())
                    .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
        }

        Sapz sapz = new Sapz();
        sapz.setTitulo(dto.titulo());
        sapz.setUnidadeCurricular(uc);
        sapz.setInstrutor(instrutor);
        sapz.setTurma(turma);
        sapz.setTipoEstrategia(dto.tipoEstrategia());
        sapz.setContextualizacao(dto.contextualizacao());
        sapz.setDesafio(dto.desafio());
        sapz.setResultadosEsperados(dto.resultadosEsperados());
        sapz.setCargaHorariaSa(dto.cargaHorariaSa());
        sapz.setCompartilhada(false);

        Sapz salva = sapzRepository.save(sapz);
        return toResponseDTO(salva);
    }

    @Transactional
    public void deletar(Long id) {
        if (!sapzRepository.existsById(id)) {
            throw new EntityNotFoundException("SAPZ não encontrada com o ID: " + id);
        }
        sapzRepository.deleteById(id);
    }

    private SapzResponseDTO toResponseDTO(Sapz sapz) {
        String nomeInstrutor = sapz.getInstrutor() != null ? sapz.getInstrutor.getNome() : null;
        return new SapzResponseDTO(
                sapz.getId(),
                sapz.getTitulo(),
                sapz.getTipoEstrategia(),
                sapz.getContextualizacao(),
                sapz.getDesafio(),
                sapz.getResultadosEsperados(),
                sapz.getCargaHorariaSa(),
                sapz.getCompartilhada(),
                nomeInstrutor,
                sapz.getDataCriacao()
        );
    }
}