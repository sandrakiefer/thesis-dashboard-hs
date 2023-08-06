package de.hsrm.mi.sandrakiefer.hsdashboard.backend.security.jwt;

/**
 * Token response object which is returned when the login is successful.
 */
public class JwtTokenResponse {

    private String token;

    public JwtTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
