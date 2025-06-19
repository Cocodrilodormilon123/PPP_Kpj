package crip.com.pe.auth_server.services;


import crip.com.pe.auth_server.dtos.RegisterAuthRequest;
import crip.com.pe.auth_server.dtos.TokenDto;
import crip.com.pe.auth_server.dtos.UserDto;

public interface AuthService {
    TokenDto login(UserDto user);
    TokenDto validateToken(TokenDto token);
    void registerUser(RegisterAuthRequest request);

}