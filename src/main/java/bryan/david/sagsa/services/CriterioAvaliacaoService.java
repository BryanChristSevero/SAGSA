package bryan.david.sagsa.services;

import bryan.david.sagsa.exceptions.ObjectNotFoundException;
import bryan.david.sagsa.models.Capacidade;
import bryan.david.sagsa.models.CriterioAvaliacao;
import bryan.david.sagsa.models.dtos.CriterioAvaliacaoDTOs;
import bryan.david.sagsa.repositories.CapacidadeRepository;
import bryan.david.sagsa.repositories.CriterioAvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CriterioAvaliacaoService {

    private final CriterioAvaliacaoRepository criterioRepository;
    private final CapacidadeRepository capacidadeRepository;

    @Transactional(readOnly = true)
    public List<CriterioAvaliacaoDTOs.Response> listarTodos() {
        return criterioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CriterioAvaliacaoDTOs.Response buscarPorId(Long id) {
        CriterioAvaliacao criterio = criterioRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Critério não encontrado com ID: " + id));
        return toResponseDTO(criterio);
    }

    @Transactional
    public CriterioAvaliacaoDTOs.Response criar(CriterioAvaliacaoDTOs.Request dto) {
        Capacidade capacidade = capacidadeRepository.findById(dto.idCapacidade())
                .orElseThrow(() -> new ObjectNotFoundException("Capacidade não encontrada com ID: " + dto.idCapacidade()));

        CriterioAvaliacao criterio = new CriterioAvaliacao();
        criterio.setDescricao(dto.descricao());
        criterio.setCapacidade(capacidade);

        CriterioAvaliacao salvo = criterioRepository.save(criterio);
        return toResponseDTO(salvo);
    }

    @Transactional
    public void deletar(Long id) {
        if (!criterioRepository.existsById(id)) {
            throw new ObjectNotFoundException("Critério não encontrado com ID: " + id);
        }
        criterioRepository.deleteById(id);
    }

    private CriterioAvaliacaoDTOs.Response toResponseDTO(CriterioAvaliacao criterio) {
        Long idCapacidade = criterio.getCapacidade() != null ? criterio.getCapacidade().getId() : null;
        return new CriterioAvaliacaoDTOs.Response(
                criterio.getId(),
                criterio.getDescricao(),
                idCapacidade
        );
    }
}