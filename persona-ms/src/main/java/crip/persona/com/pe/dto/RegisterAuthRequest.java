package crip.persona.com.pe.dto;

public class RegisterAuthRequest {
    private String username; // será el código
    private String password; // será el dni
    private String role;     // será el tipoPersona (ADMIN o ESTUDIANTE)

    // Getters y setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
