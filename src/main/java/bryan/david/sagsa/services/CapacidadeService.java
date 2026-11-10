package bryan.david.sagsa.services;

import bryan.david.sagsa.models.Capacidade;
import bryan.david.sagsa.repositories.CapacidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CapacidadeService {

    private final CapacidadeRepository capacidadeRepository;

    public List<Capacidade> listarTodas() {
        return capacidadeRepository.findAll();
    }

    public Optional<Capacidade> buscarPorId(Long id) {
        return capacidadeRepository.findById(id);
    }

    public Capacidade salvar(Capacidade capacidade) {
        return capacidadeRepository.save(capacidade);
    }

    public void deletar(Long id) {
        capacidadeRepository.deleteById(id);
    }
}