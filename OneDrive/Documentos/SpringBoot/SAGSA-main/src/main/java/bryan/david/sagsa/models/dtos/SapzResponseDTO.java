package bryan.david.sagsa.dtos;

import java.time.LocalDateTime;

public record SapzResponseDTO(
    Long id,
    String titulo,
    String tipoEstrategia,
    String contextualizacao,
    String desafio,
    String resultadosEsperados,
    Integer cargaHorariaSa,
    Boolean compartilhada,
    String nomeInstrutor,
    LocalDateTime dataCriacao
) {}