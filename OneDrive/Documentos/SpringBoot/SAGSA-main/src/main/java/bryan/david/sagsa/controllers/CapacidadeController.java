package bryan.david.sagsa.controllers;

import bryan.david.sagsa.models.dtos.CapacidadeDTOs;
import bryan.david.sagsa.services.CapacidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capacidades")
@RequiredArgsConstructor
public class CapacidadeController {

    private final CapacidadeService capacidadeService;

    @GetMapping
    public ResponseEntity<List<CapacidadeDTOs.Response>> listarTodas() {
        return ResponseEntity.ok(capacidadeService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CapacidadeDTOs.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(capacidadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CapacidadeDTOs.Response> criar(@Valid @RequestBody CapacidadeDTOs.Request dto) {
        CapacidadeDTOs.Response nova = capacidadeService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nova);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        capacidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}