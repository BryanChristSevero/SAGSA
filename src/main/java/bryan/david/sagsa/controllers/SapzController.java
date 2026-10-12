package bryan.david.sagsa.controllers;

import bryan.david.sagsa.models.Sapz;
import bryan.david.sagsa.repositories.SapzRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sapzs")
@RequiredArgsConstructor
public class SapzController {

    private final SapzRepository sapzRepository;

    @GetMapping
    public ResponseEntity<List<Sapz>> listarTodas() {
        return ResponseEntity.ok(sapzRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sapz> buscarPorId(@PathVariable Long id) {
        return sapzRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sapz> criar(@RequestBody Sapz sapz) {
        Sapz nova = sapzRepository.save(sapz);
        return ResponseEntity.status(HttpStatus.CREATED).body(nova);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!sapzRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sapzRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}