package se.jensen.anton.springer.custombeans;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom anotation for validating a password field.
 * This validation ensures that the password satisfies rules.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordRequirementValidator.class)
public @interface PasswordRequirement {
    /**
     * If the password doesn't satisfy the rules, the default validation message will be returned.
     *
     * @return the default error message
     */
    String message() default "Must be beteween 5 and 10 characters including 1 number";

    /**
     * This method returns the validation groups.
     *
     * @return the array of validation (ex. {CreateGroup.class, UpdateGroup.class})
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Bean Validation API to assign custom payload objects to a constraint.
     *
     * @return the payload classes
     */
    Class<? extends Payload>[] payload() default {};
}
