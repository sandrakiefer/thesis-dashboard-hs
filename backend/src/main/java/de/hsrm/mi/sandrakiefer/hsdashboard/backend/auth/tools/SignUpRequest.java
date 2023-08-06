package de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.tools;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.vaildation.password.ValidPassword;

/**
 * Request object for authentication sign up.
 */
public class SignUpRequest {

    @Email
    @NotEmpty
    private String email;
    @Email
    @NotEmpty
    private String emailRepetition;

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    @ValidPassword
    private String password;
    @ValidPassword
    private String passwordRepetition;

    public SignUpRequest() { }

    public SignUpRequest(String email, String emailRepetition, String firstName, String lastName, String password, String passwordRepetition) {
        this.email = email;
        this.emailRepetition = emailRepetition;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.passwordRepetition = passwordRepetition;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailRepetition() {
        return emailRepetition;
    }

    public void setEmailRepetition(String emailRepetition) {
        this.emailRepetition = emailRepetition;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepetition() {
        return passwordRepetition;
    }

    public void setPasswordRepetition(String passwordRepetition) {
        this.passwordRepetition = passwordRepetition;
    }
    
}
