package crip.gateway.com.pe.dtos;

public class TokenDto {

    private String accessToken;
    private String role;

    public TokenDto() {
    }

    public TokenDto(String accessToken, String role) {
        this.accessToken = accessToken;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Builder interno
    public static class Builder {

        private String accessToken;
        private String role;

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public TokenDto build() {
            return new TokenDto(accessToken, role);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
