package crip.com.pe.auth_server.services;

import crip.com.pe.auth_server.dtos.RegisterAuthRequest;
import crip.com.pe.auth_server.dtos.TokenDto;
import crip.com.pe.auth_server.dtos.UserDto;
import crip.com.pe.auth_server.entities.UserEntity;
import crip.com.pe.auth_server.helpers.JwtHelper;
import crip.com.pe.auth_server.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    private static final String USER_EXCEPTION_MSG = "Credenciales incorrectas";

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtHelper jwtHelper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public TokenDto login(UserDto user) {
        var userFromDb = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_EXCEPTION_MSG));

        validatePassword(user.getPassword(), userFromDb.getPassword());

        // ✅ Aquí corregido para que el token incluya el ID de persona, no el ID del registro
        var token = jwtHelper.createToken(
                userFromDb.getUsername(),
                userFromDb.getRole(),
                userFromDb.getIdPersona()
        );
        return new TokenDto(token);
    }

    @Override
    public TokenDto validateToken(TokenDto token) {
        if (jwtHelper.validateToken(token.getAccessToken())) {
            return new TokenDto(token.getAccessToken());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        System.out.println("Comparando: " + rawPassword + " vs " + encodedPassword);
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_EXCEPTION_MSG);
        }
    }

    @Override
    public void registerUser(RegisterAuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya existe");
        }

        var user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setIdPersona(request.getIdPersona()); // ✅ Guardar el ID de la persona vinculada

        userRepository.save(user);
    }
}
