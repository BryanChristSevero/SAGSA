package bryan.david.sagsa.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ModuloDTOs {

    public record Request(
        @NotNull(message = "ID do plano de curso obrigatório")
        Long planoCursoId,

        @NotBlank(message = "O nome do módulo é obrigatório")
        String nome,

        @NotBlank(message = "O tipo de módulo é obrigatório")
        String tipoModulo,

        @NotNull(message = "A carga horária é obrigatória")
        @Positive(message = "A carga horária deve ser maior que zero")
        Integer cargaHoraria,

        Integer ordem
    ) {}

    public record Response(
        Long id,
        String nome,
        String tipoModulo,
        Integer cargaHoraria,
        Integer ordem
    ) {}
}