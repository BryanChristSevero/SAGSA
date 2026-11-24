package bryan.david.sagsa.models.dtos;

import jakarta.validation.constraints.NotNull;

public class PpcDTOs {

    public record Request(
        @NotNull(message = "O ano de vigência é obrigatório")
        Integer anoVigencia,

        @NotNull(message = "O ID do curso é obrigatório")
        Long idCurso
    ) {}

    public record Response(
        Long id,
        Integer anoVigencia,
        String nomeCurso
    ) {}
}