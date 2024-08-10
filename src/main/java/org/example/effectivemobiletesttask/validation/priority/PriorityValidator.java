package org.example.effectivemobiletesttask.validation.priority;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.effectivemobiletesttask.entities.Priority;

import java.util.Arrays;

public class PriorityValidator implements ConstraintValidator<ValidPriority, String> {

    @Override
    public void initialize(ValidPriority status) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && Arrays.stream(Priority.values())
                .anyMatch(priority -> priority.name().equals(value));
    }
}
