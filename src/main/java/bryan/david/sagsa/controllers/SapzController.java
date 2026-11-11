package bryan.david.sagsa.controllers;

import bryan.david.sagsa.dtos.SapzRequestDTO;
import bryan.david.sagsa.dtos.SapzResponseDTO;
import bryan.david.sagsa.services.SapzService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sapzs")
@RequiredArgsConstructor
public class SapzController {

    private final SapzService sapzService;

    @GetMapping
    public ResponseEntity<List<SapzResponseDTO>> listarTodas() {
        return ResponseEntity.ok(sapzService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SapzResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sapzService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<SapzResponseDTO> criar(@Valid @RequestBody SapzRequestDTO dto) {
        SapzResponseDTO nova = sapzService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nova);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        sapzService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}