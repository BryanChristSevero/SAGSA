package bryan.david.sagsa.services;

import bryan.david.sagsa.exceptions.ObjectNotFoundException;
import bryan.david.sagsa.models.Capacidade;
import bryan.david.sagsa.models.Modulo;
import bryan.david.sagsa.models.dtos.CapacidadeDTOs;
import bryan.david.sagsa.repositories.CapacidadeRepository;
import bryan.david.sagsa.repositories.ModuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CapacidadeService {

    private final CapacidadeRepository capacidadeRepository;
    private final ModuloRepository moduloRepository;

    public List<CapacidadeDTOs.Response> listarTodas() {
        return capacidadeRepository.findAll().stream()
                .map(c -> new CapacidadeDTOs.Response(c.getId(), c.getTipoCapacidade(), c.getDescricao()))
                .collect(Collectors.toList());
    }

    public CapacidadeDTOs.Response buscarPorId(Long id) {
        Capacidade c = capacidadeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Capacidade não encontrada com ID: " + id));
        return new CapacidadeDTOs.Response(c.getId(), c.getTipoCapacidade(), c.getDescricao());
    }

    @Transactional
    public CapacidadeDTOs.Response criar(CapacidadeDTOs.Request dto) {
        Modulo modulo = moduloRepository.findById(dto.idModulo())
                .orElseThrow(() -> new ObjectNotFoundException("Módulo não encontrado com ID: " + dto.idModulo()));

        Capacidade capacidade = new Capacidade();
        capacidade.setTipoCapacidade(dto.tipoCapacidade());
        capacidade.setDescricao(dto.descricao());
        capacidade.setModulo(modulo);

        Capacidade salva = capacidadeRepository.save(capacidade);
        return new CapacidadeDTOs.Response(salva.getId(), salva.getTipoCapacidade(), salva.getDescricao());
    }

    @Transactional
    public void deletar(Long id) {
        buscarPorId(id);
        capacidadeRepository.deleteById(id);
    }
}