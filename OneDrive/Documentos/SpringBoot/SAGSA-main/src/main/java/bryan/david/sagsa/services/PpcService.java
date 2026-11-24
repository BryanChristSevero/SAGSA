package bryan.david.sagsa.services;

import bryan.david.sagsa.exceptions.ObjectNotFoundException;
import bryan.david.sagsa.models.Curso;
import bryan.david.sagsa.models.Ppc;
import bryan.david.sagsa.models.dtos.PpcDTOs;
import bryan.david.sagsa.repositories.CursoRepository;
import bryan.david.sagsa.repositories.PpcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PpcService {

    private final PpcRepository ppcRepository;
    private final CursoRepository cursoRepository;

    @Transactional(readOnly = true)
    public List<PpcDTOs.Response> listarTodos() {
        return ppcRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PpcDTOs.Response buscarPorId(Long id) {
        Ppc ppc = ppcRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("PPC não encontrado com ID: " + id));
        return toResponseDTO(ppc);
    }

    @Transactional
    public PpcDTOs.Response criar(PpcDTOs.Request dto) {
        Curso curso = cursoRepository.findById(dto.idCurso())
                .orElseThrow(() -> new ObjectNotFoundException("Curso não encontrado com ID: " + dto.idCurso()));

        Ppc ppc = new Ppc();
        ppc.setAnoVigencia(dto.anoVigencia());
        ppc.setCurso(curso);

        Ppc salvo = ppcRepository.save(ppc);
        return toResponseDTO(salvo);
    }

    @Transactional
    public void deletar(Long id) {
        if (!ppcRepository.existsById(id)) {
            throw new ObjectNotFoundException("PPC não encontrado com ID: " + id);
        }
        ppcRepository.deleteById(id);
    }

    private PpcDTOs.Response toResponseDTO(Ppc ppc) {
        String nomeCurso = ppc.getCurso() != null ? ppc.getCurso().getNomeCurso() : null;
        return new PpcDTOs.Response(ppc.getId(), ppc.getAnoVigencia(), nomeCurso);
    }
}