package bryan.david.sagsa.models.dtos;

import bryan.david.sagsa.models.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
    @NotBlank(message = "Nome obrigatório")
    String nome,

    @NotBlank(message = "Email obrigatório")
    @Email(message = "Formato de email inválido")
    String email,

    @NotBlank(message = "Senha obrigatória")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    String senha,

    @NotNull(message = "O tipo de perfil é obrigatório")
    Usuario.TipoPerfil tipoPerfil
) {}