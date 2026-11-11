package bryan.david.sagsa.controllers;

import bryan.david.sagsa.models.Ppc;
import bryan.david.sagsa.repositories.PpcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ppcs")
@RequiredArgsConstructor
public class PpcController {

    private final PpcRepository ppcRepository;

    @GetMapping
    public ResponseEntity<List<Ppc>> listarTodos() {
        return ResponseEntity.ok(ppcRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ppc> buscarPorId(@PathVariable Long id) {
        return ppcRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ppc> criar(@RequestBody Ppc ppc) {
        Ppc novo = ppcRepository.save(ppc);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!ppcRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ppcRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}