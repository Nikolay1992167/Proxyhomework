package by.clevertec.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InfoCarDto(

        UUID id,

        String name,

        String description,

        BigDecimal price,

        LocalDateTime created) {
}
