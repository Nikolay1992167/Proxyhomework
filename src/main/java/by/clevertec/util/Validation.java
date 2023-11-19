package by.clevertec.util;

import by.clevertec.dto.CarDto;
import by.clevertec.exception.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static by.clevertec.constants.Constants.*;

public class Validation {

    /**
     * Проверяет объект для сохранения и формирует список ошибок для выбрасывания исключения.
     *
     * @param carDto - проверяемый объект.
     */
    public void validate(CarDto carDto) {
        if (carDto == null) {
            throw new IllegalArgumentException("CarDto не может быть null");
        }

        List<String> validationErrors = new ArrayList<>();
        validateName(carDto, validationErrors);
        validateDescription(carDto, validationErrors);
        validatePrice(carDto, validationErrors);
        throwValidationException(validationErrors);
    }

    private void validateName(CarDto carDto, List<String> errors) {
        String name = carDto.name();
        if (name == null || name.trim().isEmpty()) {
            errors.add(EMPTY_NAME_ERROR);
            return;
        }
        if (!name.matches(NAME_REGEX)) {
            errors.add(INVALID_NAME_ERROR);
        }
    }

    private void validateDescription(CarDto carDto, List<String> errors) {
        String description = carDto.description();
        if (description != null && description.matches(DESCRIPTION_REGEX)) {
            errors.add(INVALID_DESCRIPTION_ERROR);
        }
    }

    private void validatePrice(CarDto carDto, List<String> errors) {
        BigDecimal price = carDto.price();
        if (price == null) {
            errors.add(NULL_PRICE_ERROR);
        } else if (price.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(INVALID_PRICE_ERROR);
        }
    }

    private void throwValidationException(List<String> errors) {
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
