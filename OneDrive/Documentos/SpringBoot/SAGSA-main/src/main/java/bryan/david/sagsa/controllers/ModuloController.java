package bryan.david.sagsa.controllers;

import bryan.david.sagsa.models.dtos.ModuloDTOs;
import bryan.david.sagsa.services.ModuloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
@RequiredArgsConstructor
public class ModuloController {

    private final ModuloService moduloService;

    @GetMapping
    public ResponseEntity<List<ModuloDTOs.Response>> listarTodos() {
        return ResponseEntity.ok(moduloService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuloDTOs.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(moduloService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ModuloDTOs.Response> criar(@Valid @RequestBody ModuloDTOs.Request dto) {
        ModuloDTOs.Response novo = moduloService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        moduloService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}