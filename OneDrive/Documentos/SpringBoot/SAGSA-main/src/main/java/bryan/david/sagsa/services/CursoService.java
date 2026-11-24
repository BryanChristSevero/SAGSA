package bryan.david.sagsa.services;

import bryan.david.sagsa.exceptions.ObjectNotFoundException;
import bryan.david.sagsa.models.Curso;
import bryan.david.sagsa.models.dtos.CursoDTOs;
import bryan.david.sagsa.repositories.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    public List<CursoDTOs.Response> listarTodos() {
        return cursoRepository.findAll().stream()
                .map(c -> new CursoDTOs.Response(c.getId(), c.getNomeCurso(), c.getEixoTecnologico(), c.getCargaHorariaTotal()))
                .collect(Collectors.toList());
    }

    public CursoDTOs.Response buscarPorId(Long id) {
        Curso c = cursoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Curso não encontrado com ID: " + id));
        return new CursoDTOs.Response(c.getId(), c.getNomeCurso(), c.getEixoTecnologico(), c.getCargaHorariaTotal());
    }

    @Transactional
    public CursoDTOs.Response criar(CursoDTOs.Request dto) {
        Curso curso = new Curso();
        curso.setNomeCurso(dto.nomeCurso());
        curso.setEixoTecnologico(dto.eixoTecnologico());
        curso.setCargaHorariaTotal(dto.cargaHorariaTotal());
        
        Curso salvo = cursoRepository.save(curso);
        return new CursoDTOs.Response(salvo.getId(), salvo.getNomeCurso(), salvo.getEixoTecnologico(), salvo.getCargaHorariaTotal());
    }

    @Transactional
    public void deletar(Long id) {
        buscarPorId(id);
        cursoRepository.deleteById(id);
    }
}