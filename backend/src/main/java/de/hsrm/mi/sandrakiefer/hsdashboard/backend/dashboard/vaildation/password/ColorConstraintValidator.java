package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.vaildation.password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@inheritDoc}
 */
public class ColorConstraintValidator implements ConstraintValidator<ValidColor, String> {

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String regex = "^#[A-Fa-f0-9]{6}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        return m.matches();
    }
    
}
