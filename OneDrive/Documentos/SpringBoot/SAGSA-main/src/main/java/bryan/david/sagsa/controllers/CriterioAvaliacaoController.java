package bryan.david.sagsa.controllers;

import bryan.david.sagsa.models.CriterioAvaliacao;
import bryan.david.sagsa.repositories.CriterioAvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/criterios")
@RequiredArgsConstructor
public class CriterioAvaliacaoController {

    private final CriterioAvaliacaoRepository criterioRepository;

    @GetMapping
    public ResponseEntity<List<CriterioAvaliacao>> listarTodos() {
        return ResponseEntity.ok(criterioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CriterioAvaliacao> buscarPorId(@PathVariable Long id) {
        return criterioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CriterioAvaliacao> criar(@RequestBody CriterioAvaliacao criterio) {
        CriterioAvaliacao novo = criterioRepository.save(criterio);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!criterioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        criterioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}