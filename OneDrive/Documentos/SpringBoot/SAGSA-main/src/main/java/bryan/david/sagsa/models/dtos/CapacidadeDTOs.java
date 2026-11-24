package bryan.david.sagsa.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CapacidadeDTOs {

    public record Request(
        @NotNull(message = "ID do módulo é obrigatório")
        Long idModulo,

        @NotBlank(message = "O tipo da capacidade é obrigatório")
        String tipoCapacidade,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao
    ) {}

    public record Response(
        Long id,
        String tipoCapacidade,
        String descricao,
        Long idModulo
    ) {}
}