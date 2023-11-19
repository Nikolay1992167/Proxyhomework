package by.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Car {

    /**
     * Идентификатор(генерируется базой)
     */
    private UUID id;

    /**
     * Название(не может быть null или пустым, содержит 5-10 символов(русский или пробелы))
     */
    private String name;

    /**
     * Описание(может быть null или 10-30 символов(русский и пробелы))
     */
    private String description;

    /**
     * Не может быть null и должен быть положительным
     */
    private BigDecimal price;

    /**
     * Время создания, не может быть null(задаётся до сохранения и не обновляется)
     */
    private LocalDateTime created;
}
