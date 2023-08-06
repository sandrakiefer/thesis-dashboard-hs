package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.vaildation.password;

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
 * ConstraintValidator that defines the logic to validate a string as color.
 * The requirments for a valid hex color string is to start with a hashtag followed by six characters (letters or numbers).
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=ColorConstraintValidator.class)
@Documented
@Size(min = 7, max = 7)
@NotEmpty
@NotNull
public @interface ValidColor {
    
    String message() default "Invalid Color";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
