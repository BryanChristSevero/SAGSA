package bryan.david.sagsa.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CursoDTOs {

    public record Request(
        @NotBlank(message = "Nome do curso é obrigatório")
        String nome,

        @NotBlank(message = "Tipo de curso é obrigatório")
        String tipoCurso,

        Long areaTecnologicaId
    ) {}

    public record Response(
        Long id,
        String nome,
        String tipoCurso,
        Boolean ativo
    ) {}
}