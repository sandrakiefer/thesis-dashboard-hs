package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.vaildation.password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@inheritDoc}
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (containsUpperCase(value) && containsLowerCase(value) && containsNumber(value) && containsSpecialCharacter(value) && !containsWhitespace(value)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the given string contains at least one capital letter.
     * 
     * @param s string to check
     * @return true if the string contains it, otherwise false
     */
    private boolean containsUpperCase(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isUpperCase(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given string contains at least one lower case letter.
     * 
     * @param s string to check
     * @return true if the string contains it, otherwise false
     */
    private boolean containsLowerCase(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLowerCase(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given string contains at least one number.
     * 
     * @param s string to check
     * @return true if the string contains it, otherwise false
     */
    private boolean containsNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given string contains at least one special character.
     * 
     * @param s string to check
     * @return true if the string contains it, otherwise false
     */
    private boolean containsSpecialCharacter(String s) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        return m.find();
    }

    /**
     * Checks if the given string contains at least one whitespace;
     * 
     * @param s string to check
     * @return true if the string contains it, otherwise false
     */
    private boolean containsWhitespace(String s) {
        return s.contains(" ");
    }
    
}
