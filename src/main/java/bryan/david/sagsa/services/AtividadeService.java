package bryan.david.sagsa.services;

import bryan.david.sagsa.models.Atividade;
import bryan.david.sagsa.repositories.AtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    public List<Atividade> listarTodas() {
        return atividadeRepository.findAll();
    }

    public Optional<Atividade> buscarPorId(Long id) {
        return atividadeRepository.findById(id);
    }

    public Atividade salvar(Atividade atividade) {
        return atividadeRepository.save(atividade);
    }

    public void deletar(Long id) {
        atividadeRepository.deleteById(id);
    }
}