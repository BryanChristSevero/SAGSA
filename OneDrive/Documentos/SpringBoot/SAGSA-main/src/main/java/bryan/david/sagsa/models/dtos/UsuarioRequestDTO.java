package bryan.david.sagsa.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
    @NotBlank(message = "nome obrigatório")
    String nome,

    @NotBlank(message = "email obrigatório")
    @Email(message = "Formato de email inválido")
    String email,

    @NotBlank(message = "senha obrigatória")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    String senha,

    @NotNull(message = "ID de perfil obrigatório")
    Long perfilId
) {}