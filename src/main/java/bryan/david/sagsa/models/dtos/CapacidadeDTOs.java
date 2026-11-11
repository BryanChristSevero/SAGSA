package bryan.david.sagsa.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CapacidadeDTOs {

    public record Request(
        @NotNull(message = "ID da unidade curricular obrigatório")
        Long unidadeCurricularId,

        @NotBlank(message = "O tipo da capacidade é obrigatório")
        String tipo,

        @NotBlank(message = "O domínio é obrigatório")
        String dominio,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao
    ) {}

    public record Response(
        Long id,
        String tipo,
        String dominio,
        String descricao
    ) {}
}