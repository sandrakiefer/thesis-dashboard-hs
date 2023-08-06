package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.vaildation.password;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Constraint validator that defines the logic to validate password for the given password as string.
 * The requirments for a valid password are at least one capital letter, one lower case letter, one number, one special character and no whitespace.
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=PasswordConstraintValidator.class)
@Documented
@Size(min=8)
@NotEmpty
@NotNull
public @interface ValidPassword {
    
    String message() default "Invalid Password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
