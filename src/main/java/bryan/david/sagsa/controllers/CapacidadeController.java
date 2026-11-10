package bryan.david.sagsa.controllers;

import bryan.david.sagsa.models.Capacidade;
import bryan.david.sagsa.repositories.CapacidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capacidades")
@RequiredArgsConstructor
public class CapacidadeController {

    private final CapacidadeRepository capacidadeRepository;

    @GetMapping
    public ResponseEntity<List<Capacidade>> listarTodas() {
        return ResponseEntity.ok(capacidadeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Capacidade> buscarPorId(@PathVariable Long id) {
        return capacidadeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Capacidade> criar(@RequestBody Capacidade capacidade) {
        Capacidade nova = capacidadeRepository.save(capacidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(nova);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!capacidadeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        capacidadeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}