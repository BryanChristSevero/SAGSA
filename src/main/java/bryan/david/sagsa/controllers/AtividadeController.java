package bryan.david.sagsa.controllers;

import bryan.david.sagsa.models.Atividade;
import bryan.david.sagsa.repositories.AtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atividades")
@RequiredArgsConstructor
public class AtividadeController {

    private final AtividadeRepository atividadeRepository;

    @GetMapping
    public ResponseEntity<List<Atividade>> listarTodas() {
        return ResponseEntity.ok(atividadeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atividade> buscarPorId(@PathVariable Long id) {
        return atividadeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Atividade> criar(@RequestBody Atividade atividade) {
        Atividade nova = atividadeRepository.save(atividade);
        return ResponseEntity.status(HttpStatus.CREATED).body(nova);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!atividadeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        atividadeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}