package util;

import by.clevertec.dto.CarDto;
import by.clevertec.dto.InfoCarDto;
import by.clevertec.entity.Car;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static util.Constants.*;

@Data
@Builder(setterPrefix = "with")
public class CarTestData {

    @Builder.Default
    private UUID id = CAR_UUID;
    @Builder.Default
    private String name = CAR_NAME;
    @Builder.Default
    private String description = CAR_DESCRIPTION;
    @Builder.Default
    private BigDecimal price = CAR_PRICE;
    @Builder.Default
    private LocalDateTime created = CAR_CREATED;

    public Car buildCar() {
        return new Car(id, name, description, price, created);
    }

    public CarDto buildCarDto() {
        return new CarDto(name, description, price);
    }

    public InfoCarDto buildInfoCarDto() {
        return new InfoCarDto(id, name, description, price, created);
    }
}
