package by.clevertec.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public record CarDto(
        @NotBlank(message = "Name cannot be null or empty.")
        @Size(min = 5, max = 10, message = "Name must be between 5 and 10 characters.")
        @Pattern(regexp = "[А-Яа-я ]+", message = "Name must contain only Russian letters or spaces.")
        String name,

        @Size (min = 10, max = 30, message = "Description must be between 10 and 30 characters.")
        @Pattern (regexp = "[А-Яа-я ]*", message = "Description must contain only Russian letters or spaces.")
        String description,

        @NotNull(message = "Price cannot be null.")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive.")
        BigDecimal price) {
}
