package se.jensen.anton.springer.custombeans;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordRequirementValidator implements ConstraintValidator<PasswordRequirement, String> {
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
