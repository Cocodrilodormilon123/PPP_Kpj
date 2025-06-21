package crip.com.pe.auth_server.controllers;

import crip.com.pe.auth_server.dtos.RegisterAuthRequest;
import crip.com.pe.auth_server.dtos.TokenDto;
import crip.com.pe.auth_server.dtos.UserDto;
import crip.com.pe.auth_server.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserDto user) {
        log.info("Autenticando usuario: {}", user.getUsername());
        return ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("/jwt")
    public ResponseEntity<TokenDto> validate(@RequestHeader("accessToken") String accessToken) {
        try {
            log.info("Validando token...");
            TokenDto tokenDto = authService.validateToken(new TokenDto(accessToken));
            log.info("Token válido");
            return ResponseEntity.ok(tokenDto);
        } catch (Exception e) {
            log.error("Error al validar token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterAuthRequest request) {
        log.info("Registrando usuario: {}", request.getUsername());
        authService.registerUser(request);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}