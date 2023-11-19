package by.clevertec.util;

import by.clevertec.dto.CarDto;
import by.clevertec.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import util.CarTestData;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class ValidationTest {

    @InjectMocks
    Validation validation;

    @Test
    void shouldNotReturnException() {
        // given
        CarDto carDto = CarTestData.builder()
                .build()
                .buildCarDto();
        // when, then
        assertThatCode(() -> validation.validate(carDto)).doesNotThrowAnyException();
    }

    @Test
    void shouldReturnExceptionWhenNameInvalid() {
        // given
        CarDto carDto = CarTestData.builder()
                .withName("Toyota")
                .build()
                .buildCarDto();

        // when, then
        assertThatThrownBy(() -> validation.validate(carDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Неверное имя car!");
    }

    @Test
    void shouldReturnExceptionWhenDescriptionInvalid() {
        // given
        CarDto carDto = CarTestData.builder()
                .withDescription("Invalid Description")
                .build()
                .buildCarDto();

        // when, then
        assertThatThrownBy(() -> validation.validate(carDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Неверное описание car!");
    }

    @Test
    void shouldReturnExceptionWhenPriceInvalid() {
        // given
        CarDto carDto = CarTestData.builder()
                .withPrice(BigDecimal.valueOf(-1000))
                .build()
                .buildCarDto();

        // when, then
        assertThatThrownBy(() -> validation.validate(carDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Корректно укажите цену, она не может быть меньше либо равна 0");
    }
}