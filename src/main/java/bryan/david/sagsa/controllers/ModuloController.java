package bryan.david.sagsa.controllers;

import bryan.david.sagsa.models.Modulo;
import bryan.david.sagsa.repositories.ModuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
@RequiredArgsConstructor
public class ModuloController {

    private final ModuloRepository moduloRepository;

    @GetMapping
    public ResponseEntity<List<Modulo>> listarTodos() {
        return ResponseEntity.ok(moduloRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modulo> buscarPorId(@PathVariable Long id) {
        return moduloRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Modulo> criar(@RequestBody Modulo modulo) {
        Modulo novo = moduloRepository.save(modulo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!moduloRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        moduloRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}