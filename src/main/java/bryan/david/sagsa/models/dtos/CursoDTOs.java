package bryan.david.sagsa.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CursoDTOs {

    public record Request(
        @NotBlank(message = "O nome do curso é obrigatório")
        String nomeCurso,

        @NotBlank(message = "O eixo tecnológico é obrigatório")
        String eixoTecnologico,

        @NotNull(message = "A carga horária total é obrigatória")
        @Positive(message = "A carga horária deve ser maior que zero")
        Integer cargaHorariaTotal
    ) {}

    public record Response(
        Long id,
        String nomeCurso,
        String eixoTecnologico,
        Integer cargaHorariaTotal
    ) {}
}