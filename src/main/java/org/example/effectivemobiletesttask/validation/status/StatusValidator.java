package org.example.effectivemobiletesttask.validation.status;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.effectivemobiletesttask.entities.Status;

import java.util.Arrays;

public class StatusValidator implements ConstraintValidator<ValidStatus, String> {

    @Override
    public void initialize(ValidStatus status) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && Arrays.stream(Status.values())
                .anyMatch(status -> status.name().equals(value));
    }
}
