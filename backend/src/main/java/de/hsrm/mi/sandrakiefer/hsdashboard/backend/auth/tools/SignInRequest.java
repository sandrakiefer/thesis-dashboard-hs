package de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.tools;

import javax.validation.constraints.Email;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.vaildation.password.ValidPassword;

/**
 * Request object for authentication sign in.
 */
public class SignInRequest {

    @Email
    private String email;

    @ValidPassword
    private String password;

    public SignInRequest() { }

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
