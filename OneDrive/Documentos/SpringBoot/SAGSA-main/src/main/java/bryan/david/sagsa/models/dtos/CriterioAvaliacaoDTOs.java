package bryan.david.sagsa.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CriterioAvaliacaoDTOs {

    public record Request(
        @NotBlank(message = "A descrição do critério é obrigatória")
        String descricao,

        @NotNull(message = "O ID da capacidade é obrigatório")
        Long idCapacidade
    ) {}

    public record Response(
        Long id,
        String descricao,
        Long idCapacidade
    ) {}
}