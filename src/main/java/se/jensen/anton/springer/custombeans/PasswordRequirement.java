package se.jensen.anton.springer.custombeans;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordRequirementValidator.class)
public @interface PasswordRequirement {

    String message() default "Must be beteween 5 and 10 characters including 1 number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
