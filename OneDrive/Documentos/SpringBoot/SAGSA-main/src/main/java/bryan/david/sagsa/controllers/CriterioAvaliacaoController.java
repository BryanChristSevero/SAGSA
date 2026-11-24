package bryan.david.sagsa.controllers;

import bryan.david.sagsa.models.dtos.CriterioAvaliacaoDTOs;
import bryan.david.sagsa.services.CriterioAvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/criterios")
@RequiredArgsConstructor
public class CriterioAvaliacaoController {

    private final CriterioAvaliacaoService criterioService;

    @GetMapping
    public ResponseEntity<List<CriterioAvaliacaoDTOs.Response>> listarTodos() {
        return ResponseEntity.ok(criterioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CriterioAvaliacaoDTOs.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(criterioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CriterioAvaliacaoDTOs.Response> criar(@Valid @RequestBody CriterioAvaliacaoDTOs.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(criterioService.criar(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        criterioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}