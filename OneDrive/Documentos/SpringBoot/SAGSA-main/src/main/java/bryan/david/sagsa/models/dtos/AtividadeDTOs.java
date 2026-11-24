package bryan.david.sagsa.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AtividadeDTOs {

    public record Request(
        @NotBlank(message = "A descrição da atividade é obrigatória")
        String descricaoAtividade,

        @NotNull(message = "A carga horária é obrigatória")
        @Positive(message = "A carga horária deve ser maior que zero")
        Integer cargaHoraria,

        @NotNull(message = "O ID da SAPZ é obrigatório")
        Long idSapz
    ) {}

    public record Response(
        Long id,
        String descricaoAtividade,
        Integer cargaHoraria,
        Long idSapz
    ) {}
}