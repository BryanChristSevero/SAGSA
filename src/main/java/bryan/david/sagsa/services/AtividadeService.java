package bryan.david.sagsa.services;

import bryan.david.sagsa.exceptions.ObjectNotFoundException;
import bryan.david.sagsa.models.Atividade;
import bryan.david.sagsa.models.Sapz;
import bryan.david.sagsa.models.dtos.AtividadeDTOs;
import bryan.david.sagsa.repositories.AtividadeRepository;
import bryan.david.sagsa.repositories.SapzRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final SapzRepository sapzRepository;

    @Transactional(readOnly = true)
    public List<AtividadeDTOs.Response> listarTodas() {
        return atividadeRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public AtividadeDTOs.Response buscarPorId(Long id) {
        Atividade atividade = atividadeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Atividade não encontrada com ID: " + id));
        return toResponseDTO(atividade);
    }

    @Transactional
    public AtividadeDTOs.Response criar(AtividadeDTOs.Request dto) {
        Sapz sapz = sapzRepository.findById(dto.idSapz())
                .orElseThrow(() -> new ObjectNotFoundException("SAPZ não encontrada com ID: " + dto.idSapz()));

        Atividade atividade = new Atividade();
        atividade.setDescricaoAtividade(dto.descricaoAtividade());
        atividade.setCargaHoraria(dto.cargaHoraria());
        atividade.setSapz(sapz);

        Atividade salva = atividadeRepository.save(atividade);
        return toResponseDTO(salva);
    }

    @Transactional
    public void deletar(Long id) {
        if (!atividadeRepository.existsById(id)) {
            throw new ObjectNotFoundException("Atividade não encontrada com ID: " + id);
        }
        atividadeRepository.deleteById(id);
    }

    private AtividadeDTOs.Response toResponseDTO(Atividade atividade) {
        Long idSapz = atividade.getSapz() != null ? atividade.getSapz().getId() : null;
        return new AtividadeDTOs.Response(
                atividade.getId(),
                atividade.getDescricaoAtividade(),
                atividade.getCargaHoraria(),
                idSapz
        );
    }
}