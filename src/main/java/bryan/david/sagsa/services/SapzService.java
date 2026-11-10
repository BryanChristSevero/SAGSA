package bryan.david.sagsa.services;

import bryan.david.sagsa.models.Sapz;
import bryan.david.sagsa.repositories.SapzRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SapzService {

    private final SapzRepository sapzRepository;

    public List<Sapz> listarTodas() {
        return sapzRepository.findAll();
    }

    public Optional<Sapz> buscarPorId(Long id) {
        return sapzRepository.findById(id);
    }

    public Sapz salvar(Sapz sapz) {
        return sapzRepository.save(sapz);
    }

    public void deletar(Long id) {
        sapzRepository.deleteById(id);
    }
}