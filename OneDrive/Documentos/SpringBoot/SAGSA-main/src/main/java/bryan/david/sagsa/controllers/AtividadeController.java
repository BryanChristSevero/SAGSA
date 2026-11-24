package bryan.david.sagsa.controllers;

import bryan.david.sagsa.models.dtos.AtividadeDTOs;
import bryan.david.sagsa.services.AtividadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atividades")
@RequiredArgsConstructor
public class AtividadeController {

    private final AtividadeService atividadeService;

    @GetMapping
    public ResponseEntity<List<AtividadeDTOs.Response>> listarTodas() {
        return ResponseEntity.ok(atividadeService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtividadeDTOs.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(atividadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AtividadeDTOs.Response> criar(@Valid @RequestBody AtividadeDTOs.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atividadeService.criar(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        atividadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}