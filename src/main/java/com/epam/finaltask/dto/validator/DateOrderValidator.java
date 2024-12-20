package com.epam.finaltask.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateOrderValidator implements ConstraintValidator<DateOrder, Object> {
    private String firstFieldName;
    private String secondFieldName;


    @Override
    public void initialize(DateOrder annotation) {
        firstFieldName = annotation.firstDate();
        secondFieldName = annotation.secondDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            LocalDate firstDate = (LocalDate) getFieldValue(value, firstFieldName);
            LocalDate secondDate = (LocalDate) getFieldValue(value, secondFieldName);

            return firstDate.isBefore(secondDate) || firstDate.isEqual(secondDate);
        } catch (Exception e) {
            return false;
        }
    }


    private Object getFieldValue(Object value, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = value.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(value);
    }
}
