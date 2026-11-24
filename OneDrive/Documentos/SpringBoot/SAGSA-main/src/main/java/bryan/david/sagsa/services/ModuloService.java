package bryan.david.sagsa.services;

import bryan.david.sagsa.exceptions.ObjectNotFoundException;
import bryan.david.sagsa.models.Modulo;
import bryan.david.sagsa.models.Ppc;
import bryan.david.sagsa.models.dtos.ModuloDTOs;
import bryan.david.sagsa.repositories.ModuloRepository;
import bryan.david.sagsa.repositories.PpcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuloService {

    private final ModuloRepository moduloRepository;
    private final PpcRepository ppcRepository;

    public List<ModuloDTOs.Response> listarTodos() {
        return moduloRepository.findAll().stream()
                .map(m -> new ModuloDTOs.Response(m.getId(), m.getNomeModulo(), m.getDescricao(),
                        m.getPpc() != null ? m.getPpc().getId() : null))
                .collect(Collectors.toList());
    }

    public ModuloDTOs.Response buscarPorId(Long id) {
        Modulo m = moduloRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Módulo não encontrado com ID: " + id));
        return new ModuloDTOs.Response(m.getId(), m.getNomeModulo(), m.getDescricao(),
                m.getPpc() != null ? m.getPpc().getId() : null);
    }

    @Transactional
    public ModuloDTOs.Response criar(ModuloDTOs.Request dto) {
        Ppc ppc = ppcRepository.findById(dto.idPpc())
                .orElseThrow(() -> new ObjectNotFoundException("PPC não encontrado com ID: " + dto.idPpc()));

        Modulo modulo = new Modulo();
        modulo.setNomeModulo(dto.nomeModulo());
        modulo.setDescricao(dto.descricao());
        modulo.setPpc(ppc);

        Modulo salvo = moduloRepository.save(modulo);
        return new ModuloDTOs.Response(salvo.getId(), salvo.getNomeModulo(), salvo.getDescricao(),
                salvo.getPpc() != null ? salvo.getPpc().getId() : null);
    }

    @Transactional
    public void deletar(Long id) {
        buscarPorId(id);
        moduloRepository.deleteById(id);
    }
}