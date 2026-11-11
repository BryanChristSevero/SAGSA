package bryan.david.sagsa.services;

import bryan.david.sagsa.models.Modulo;
import bryan.david.sagsa.repositories.ModuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModuloService {

    private final ModuloRepository moduloRepository;

    public List<Modulo> listarTodos() {
        return moduloRepository.findAll();
    }

    public Optional<Modulo> buscarPorId(Long id) {
        return moduloRepository.findById(id);
    }

    public Modulo salvar(Modulo modulo) {
        return moduloRepository.save(modulo);
    }

    public void deletar(Long id) {
        moduloRepository.deleteById(id);
    }
}