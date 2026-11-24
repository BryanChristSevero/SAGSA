package bryan.david.sagsa.controllers;

import bryan.david.sagsa.security.JWTUtil;
import bryan.david.sagsa.security.UserSpringSecurity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    // DTO interno para receber as credenciais de login
    public record LoginRequest(
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String senha
    ) {}

    // DTO interno para retornar o token gerado
    public record LoginResponse(
        String token
    ) {}

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Dispara a autenticação gerada pelo Spring Security usando o UserDetailsServiceImpl
        Authentication authToken = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        Authentication authResult = authenticationManager.authenticate(authToken);

        // Obtém o utilizador autenticado com sucesso e gera o token JWT
        UserSpringSecurity user = (UserSpringSecurity) authResult.getPrincipal();
        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}