package se.jensen.anton.springer.custombeans;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validate fields annotated with {@link PasswordRequirement}.
 */
public class PasswordRequirementValidator implements ConstraintValidator<PasswordRequirement, String> {
    /**
     *
     * This method checks whether the given password satisfies the rules.
     *
     * @param password The password to validate
     * @param context  The context in which the constraint is evaluated
     * @return True: if the password is null or satisfies the requirements. False: if the password is not valid.
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true;
        }
        boolean lengthOk = password.length() >= 5 && password.length() <= 10;
        boolean containsNumber = password.matches(".*\\d.*");

        return lengthOk && containsNumber;
    }
}
