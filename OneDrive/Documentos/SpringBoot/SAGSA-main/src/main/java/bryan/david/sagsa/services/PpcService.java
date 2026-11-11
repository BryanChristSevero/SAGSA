package bryan.david.sagsa.services;

import bryan.david.sagsa.models.Ppc;
import bryan.david.sagsa.repositories.PpcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PpcService {

    private final PpcRepository ppcRepository;

    public List<Ppc> listarTodos() {
        return ppcRepository.findAll();
    }

    public Optional<Ppc> buscarPorId(Long id) {
        return ppcRepository.findById(id);
    }

    public Ppc salvar(Ppc ppc) {
        return ppcRepository.save(ppc);
    }

    public void deletar(Long id) {
        ppcRepository.deleteById(id);
    }
}