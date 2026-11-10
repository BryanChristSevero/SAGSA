package bryan.david.sagsa.services;

import bryan.david.sagsa.models.CriterioAvaliacao;
import bryan.david.sagsa.repositories.CriterioAvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CriterioAvaliacaoService {

    private final CriterioAvaliacaoRepository criterioAvaliacaoRepository;

    public List<CriterioAvaliacao> listarTodos() {
        return criterioAvaliacaoRepository.findAll();
    }

    public Optional<CriterioAvaliacao> buscarPorId(Long id) {
        return criterioAvaliacaoRepository.findById(id);
    }

    public CriterioAvaliacao salvar(CriterioAvaliacao criterioAvaliacao) {
        return criterioAvaliacaoRepository.save(criterioAvaliacao);
    }

    public void deletar(Long id) {
        criterioAvaliacaoRepository.deleteById(id);
    }
}