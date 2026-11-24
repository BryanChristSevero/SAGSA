package bryan.david.sagsa.models.dtos;

import java.time.LocalDateTime;

public record SapzResponseDTO(
    Long id,
    String titulo,
    String descricao,
    String desafio,
    LocalDateTime dataGeracao,
    Integer cargaHorariaTotal,
    String nomeUsuario
) {}