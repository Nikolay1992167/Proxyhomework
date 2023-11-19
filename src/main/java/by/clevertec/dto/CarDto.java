package by.clevertec.dto;

import java.math.BigDecimal;

public record CarDto(

        String name,

        String description,

        BigDecimal price) {
}
