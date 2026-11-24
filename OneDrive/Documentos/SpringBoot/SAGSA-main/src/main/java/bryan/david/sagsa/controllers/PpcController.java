package bryan.david.sagsa.controllers;

import bryan.david.sagsa.models.dtos.PpcDTOs;
import bryan.david.sagsa.services.PpcService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ppcs")
@RequiredArgsConstructor
public class PpcController {

    private final PpcService ppcService;

    @GetMapping
    public ResponseEntity<List<PpcDTOs.Response>> listarTodos() {
        return ResponseEntity.ok(ppcService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PpcDTOs.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ppcService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PpcDTOs.Response> criar(@Valid @RequestBody PpcDTOs.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ppcService.criar(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ppcService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}